(function(){
  const health={
    version:'6.0.0',
    checkedAt:new Date().toISOString(),
    serviceWorker:'serviceWorker' in navigator,
    indexedDB:'indexedDB' in window,
    mediaRecorder:'MediaRecorder' in window,
    online:navigator.onLine
  };
  try{localStorage.setItem('em_last_health_check',JSON.stringify(health));}catch(_){}
  window.addEventListener('error',e=>{
    try{
      const logs=JSON.parse(localStorage.getItem('em_error_logs')||'[]');
      logs.push({type:'window-error',message:e.message||'Unknown error',time:new Date().toISOString()});
      localStorage.setItem('em_error_logs',JSON.stringify(logs.slice(-100)));
    }catch(_){}
  });
  window.addEventListener('unhandledrejection',e=>{
    try{
      const logs=JSON.parse(localStorage.getItem('em_error_logs')||'[]');
      logs.push({type:'unhandled-rejection',message:String(e.reason||'Unknown rejection'),time:new Date().toISOString()});
      localStorage.setItem('em_error_logs',JSON.stringify(logs.slice(-100)));
    }catch(_){}
  });
})();
