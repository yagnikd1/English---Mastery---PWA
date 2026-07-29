(() => {
  let deferredInstallPrompt = null;
  let registration = null;

  function show(id, visible = true) {
    const element = document.getElementById(id);
    if (element) element.hidden = !visible;
  }

  function setText(id, text) {
    const element = document.getElementById(id);
    if (element) element.textContent = text;
  }

  window.addEventListener('beforeinstallprompt', event => {
    event.preventDefault();
    deferredInstallPrompt = event;
    show('installAppButton', true);
    setText('installStatus', 'This device is ready to install the app.');
  });

  window.addEventListener('appinstalled', () => {
    deferredInstallPrompt = null;
    show('installAppButton', false);
    setText('installStatus', 'English Mastery is installed.');
  });

  async function installApp() {
    if (!deferredInstallPrompt) {
      setText('installStatus', 'Use the browser menu and choose “Add to Home screen” or “Install app”.');
      return;
    }
    deferredInstallPrompt.prompt();
    const result = await deferredInstallPrompt.userChoice;
    setText('installStatus', result.outcome === 'accepted'
      ? 'Installation accepted.'
      : 'Installation was cancelled.');
    deferredInstallPrompt = null;
    show('installAppButton', false);
  }

  function announceUpdate(worker) {
    show('applyUpdateButton', true);
    setText('updateStatus', 'A new app version is ready.');
    document.getElementById('applyUpdateButton')?.addEventListener('click', () => {
      worker.postMessage({type: 'SKIP_WAITING'});
    }, {once: true});
  }

  async function registerServiceWorker() {
    if (!('serviceWorker' in navigator)) {
      setText('updateStatus', 'Service workers are not supported in this browser.');
      return;
    }

    try {
      registration = await navigator.serviceWorker.register('./service-worker.js');

      if (registration.waiting) announceUpdate(registration.waiting);

      registration.addEventListener('updatefound', () => {
        const worker = registration.installing;
        worker?.addEventListener('statechange', () => {
          if (worker.state === 'installed' && navigator.serviceWorker.controller) {
            announceUpdate(worker);
          }
        });
      });

      navigator.serviceWorker.addEventListener('controllerchange', () => {
        if (!sessionStorage.getItem('em_update_reloaded')) {
          sessionStorage.setItem('em_update_reloaded', '1');
          location.reload();
        }
      });

      setText('updateStatus', 'App files are ready for offline use.');
    } catch (error) {
      setText('updateStatus', `Offline setup failed: ${error.message}`);
    }
  }

  document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('installAppButton')?.addEventListener('click', installApp);
    registerServiceWorker();
  });
})();
