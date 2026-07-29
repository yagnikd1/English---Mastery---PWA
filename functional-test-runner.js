(() => {
  const results = [];
  const add = (name, pass, detail='') => results.push({name, pass:!!pass, detail});
  async function run() {
    results.length = 0;
    try {
      add('Secure context', location.protocol === 'https:' || ['localhost','127.0.0.1'].includes(location.hostname), location.href);
      add('Service Worker API', 'serviceWorker' in navigator);
      add('IndexedDB API', 'indexedDB' in window);
      add('Cache Storage API', 'caches' in window);
      add('MediaRecorder API', 'MediaRecorder' in window);
      add('Microphone API', !!navigator.mediaDevices?.getUserMedia);
      add('Storage API', !!navigator.storage?.estimate);

      const [a,c,m] = await Promise.all([
        fetch('content/audio-catalog.json'),
        fetch('content/30-day-course-index.json'),
        fetch('manifest.json')
      ]);
      add('Audio catalog fetch', a.ok, `HTTP ${a.status}`);
      add('Course index fetch', c.ok, `HTTP ${c.status}`);
      add('Manifest fetch', m.ok, `HTTP ${m.status}`);

      if (a.ok) {
        const data = await a.json();
        add('Public audio catalog loaded', Array.isArray(data.tracks), `${data.tracks?.length || 0} public tracks`);
      }
      if (c.ok) {
        const data = await c.json();
        add('30-lesson course', data.lessonCount === 30, `${data.lessonCount || 0} lessons`);
        add('Five steps per lesson', data.days?.every(x => x.stepCount === 5));
      }

      const reg = await navigator.serviceWorker?.getRegistration();
      add('Service worker registered', !!reg, reg?.scope || 'Not registered yet');

      if (navigator.storage?.estimate) {
        const {usage=0, quota=0} = await navigator.storage.estimate();
        add('Storage quota available', quota > 0, `${Math.round(usage/1048576)} MB / ${Math.round(quota/1048576)} MB`);
      }
    } catch (e) {
      add('Runner completed', false, String(e.message || e));
    }

    const passed = results.filter(x => x.pass).length;
    document.getElementById('testSummary').textContent = `${passed}/${results.length} automated checks passed.`;
    document.getElementById('testResults').innerHTML = results.map(x =>
      `<div class="test-result ${x.pass?'pass':'fail'}"><b>${x.pass?'✓':'✕'} ${x.name}</b><small>${x.detail}</small></div>`
    ).join('');
    sessionStorage.setItem('em_stage16_test_report', JSON.stringify({
      version:'16.0.0', testedAt:new Date().toISOString(), userAgent:navigator.userAgent, results
    }));
  }

  function download() {
    const raw = sessionStorage.getItem('em_stage16_test_report');
    if (!raw) return;
    const u = URL.createObjectURL(new Blob([raw], {type:'application/json'}));
    const a = document.createElement('a');
    a.href=u; a.download='english-mastery-stage16-browser-test-report.json'; a.click();
    URL.revokeObjectURL(u);
  }

  document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('runFunctionalTests')?.addEventListener('click', run);
    document.getElementById('downloadFunctionalReport')?.addEventListener('click', download);
  });
})();
