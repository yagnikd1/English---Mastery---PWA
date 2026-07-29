(() => {
  const TEST_KEY='em_stage16_test_results';
  const tests=[];
  const add=(id,label,run)=>tests.push({id,label,run});
  const save=results=>localStorage.setItem(TEST_KEY,JSON.stringify({updatedAt:new Date().toISOString(),results}));
  const load=()=>{try{return JSON.parse(localStorage.getItem(TEST_KEY)||'null')}catch(_){return null}};

  add('secure-context','Secure context',async()=>({pass:window.isSecureContext,detail:window.isSecureContext?'HTTPS/localhost detected':'Install and microphone need HTTPS or localhost'}));
  add('service-worker','Service worker support',async()=>({pass:'serviceWorker' in navigator,detail:'serviceWorker' in navigator?'Supported':'Not supported'}));
  add('indexeddb','IndexedDB support',async()=>({pass:'indexedDB' in window,detail:'indexedDB' in window?'Supported':'Not supported'}));
  add('storage-write','Local storage write/read',async()=>{try{localStorage.setItem('__em_stage16','ok');const ok=localStorage.getItem('__em_stage16')==='ok';localStorage.removeItem('__em_stage16');return{pass:ok,detail:ok?'Read/write works':'Read/write failed'}}catch(e){return{pass:false,detail:e.message}}});
  add('media-recorder','Microphone recording API',async()=>({pass:'MediaRecorder' in window,detail:'MediaRecorder' in window?'Available':'Unavailable in this browser'}));
  add('audio','HTML audio support',async()=>{const a=document.createElement('audio');const p=!!a.canPlayType('audio/mpeg');return{pass:p,detail:p?'MP3 playback supported':'MP3 support not reported'}});
  add('online-state','Network state API',async()=>({pass:'onLine' in navigator,detail:navigator.onLine?'Currently online':'Currently offline'}));
  add('manifest','Manifest reachable',async()=>{try{const r=await fetch('./manifest.json',{cache:'no-store'});return{pass:r.ok,detail:r.ok?'Loaded':'HTTP '+r.status}}catch(e){return{pass:false,detail:e.message}}});
  add('catalog','Public audio catalog reachable',async()=>{try{const r=await fetch('./content/audio-catalog.json');if(!r.ok)return{pass:false,detail:'HTTP '+r.status};const d=await r.json();return{pass:Array.isArray(d.tracks),detail:(d.tracks?.length||0)+' public tracks; private audio stays local'}}catch(e){return{pass:false,detail:e.message}}});

  async function runAll(){
    const host=document.getElementById('androidTestResults'); if(!host)return;
    host.innerHTML='<p class="muted">Running tests…</p>';
    const results=[];
    for(const t of tests){let result;try{result=await t.run()}catch(e){result={pass:false,detail:e.message}}results.push({...t,result});render(results);save(results.map(x=>({id:x.id,label:x.label,...x.result})));}
  }
  function render(results){
    const host=document.getElementById('androidTestResults'); if(!host)return;
    const passed=results.filter(x=>x.result.pass).length;
    host.innerHTML=`<div class="test-summary"><b>${passed}/${results.length} checks passed</b><span>${results.length-passed} need attention</span></div>`+results.map(x=>`<div class="test-row ${x.result.pass?'pass':'fail'}"><span>${x.result.pass?'✓':'!'}</span><div><b>${x.label}</b><small>${x.result.detail||''}</small></div></div>`).join('');
  }
  function exportReport(){const data=load()||{updatedAt:new Date().toISOString(),results:[]};data.userAgent=navigator.userAgent;data.url=location.href;data.online=navigator.onLine;const blob=new Blob([JSON.stringify(data,null,2)],{type:'application/json'});const a=document.createElement('a');a.href=URL.createObjectURL(blob);a.download='english-mastery-android-test-report.json';a.click();URL.revokeObjectURL(a.href)}
  document.addEventListener('DOMContentLoaded',()=>{document.getElementById('runAndroidTests')?.addEventListener('click',runAll);document.getElementById('exportAndroidTests')?.addEventListener('click',exportReport);const old=load();if(old?.results){const converted=old.results.map(x=>({label:x.label,result:{pass:x.pass,detail:x.detail}}));render(converted)}});
})();
