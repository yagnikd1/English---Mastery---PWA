package com.englishmastery.offline;

final class AndroidEnhancements {
    private AndroidEnhancements() { }

    static final String JS = """
            (function(){
              if(window.__englishMasteryAndroidV103)return;
              window.__englishMasteryAndroidV103=true;

              window.download=function(data,name){
                AndroidBridge.saveJson(JSON.stringify(data,null,2),name||'english-mastery-backup.json');
              };

              var style=document.createElement('style');
              style.id='english-mastery-android-v103';
              style.textContent=`
                .topbar{padding-top:6px!important}
                body.light{--muted:#465467!important;--line:#c7d0dc!important;--card2:#e8edf5!important;--accent2:#087f74!important}
                body.light .pill,body.light .tag{background:#dcf5f0!important;color:#087f74!important;border-color:#97d8cd!important}
                body.light .module-no,body.light .eyebrow{color:#087f74!important}
                body.light .secondary{color:#101828!important;border-color:#b7c2d0!important}
                body.light .daily-task p,body.light .muted,body.light .empty-review,body.light .weekly-summary-grid span,body.light .daily-plan-summary span{color:#465467!important}
                #androidAppPanel .button-stack,#androidHelpPanel .button-stack{margin-top:12px}

                .app-shell{padding-bottom:116px!important}
                .bottom-nav{
                  min-height:80px!important;
                  display:grid!important;
                  grid-template-columns:repeat(5,minmax(0,1fr))!important;
                  gap:4px!important;
                  padding:8px 8px calc(8px + env(safe-area-inset-bottom))!important;
                  background:rgba(11,17,29,.98)!important;
                  border-top:1px solid rgba(203,213,225,.16)!important;
                  box-shadow:0 -10px 28px rgba(0,0,0,.24)!important;
                  backdrop-filter:blur(20px)!important;
                  -webkit-backdrop-filter:blur(20px)!important;
                }
                .nav-item{
                  min-width:0!important;
                  min-height:64px!important;
                  padding:6px 2px!important;
                  border-radius:18px!important;
                  display:flex!important;
                  flex-direction:column!important;
                  align-items:center!important;
                  justify-content:center!important;
                  gap:5px!important;
                  color:#a9b4c4!important;
                  font-family:Roboto,Inter,system-ui,-apple-system,Segoe UI,sans-serif!important;
                  transition:background-color .18s ease,color .18s ease,transform .18s ease!important;
                  -webkit-tap-highlight-color:transparent!important;
                }
                .nav-item:active{transform:scale(.97)!important}
                .nav-item span{
                  display:block!important;
                  width:24px!important;
                  height:24px!important;
                  flex:0 0 24px!important;
                  font-size:0!important;
                  line-height:0!important;
                  background-color:currentColor!important;
                  -webkit-mask-repeat:no-repeat!important;
                  -webkit-mask-position:center!important;
                  -webkit-mask-size:24px 24px!important;
                  mask-repeat:no-repeat!important;
                  mask-position:center!important;
                  mask-size:24px 24px!important;
                }
                .nav-item small{
                  display:block!important;
                  margin:0!important;
                  font-family:Roboto,Inter,system-ui,-apple-system,Segoe UI,sans-serif!important;
                  font-size:13px!important;
                  line-height:16px!important;
                  font-weight:600!important;
                  letter-spacing:.01em!important;
                  white-space:nowrap!important;
                  color:currentColor!important;
                }
                .nav-item.active{color:#54d6c6!important;background:rgba(84,214,198,.16)!important}
                .nav-item.active small{font-weight:800!important}
                .nav-item:nth-child(1) span{-webkit-mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M12 3 2 12h3v9h5v-6h4v6h5v-9h3L12 3z'/%3E%3C/svg%3E")!important;mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M12 3 2 12h3v9h5v-6h4v6h5v-9h3L12 3z'/%3E%3C/svg%3E")!important}
                .nav-item:nth-child(2) span{-webkit-mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M21 4H3c-1.1 0-2 .9-2 2v13c0 1.1.9 2 2 2h18c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 15h-8V6h8v13zm-10 0H3V6h8v13z'/%3E%3C/svg%3E")!important;mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M21 4H3c-1.1 0-2 .9-2 2v13c0 1.1.9 2 2 2h18c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 15h-8V6h8v13zm-10 0H3V6h8v13z'/%3E%3C/svg%3E")!important}
                .nav-item:nth-child(3) span{-webkit-mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='m3.5 18.49 6-6 4 4L22 6.92 20.59 5.5l-7.09 7.09-4-4L2 17.08l1.5 1.41z'/%3E%3C/svg%3E")!important;mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='m3.5 18.49 6-6 4 4L22 6.92 20.59 5.5l-7.09 7.09-4-4L2 17.08l1.5 1.41z'/%3E%3C/svg%3E")!important}
                .nav-item:nth-child(4) span{-webkit-mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M10 4H2C.9 4 0 4.9 0 6v12c0 1.1.9 2 2 2h20c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2H12l-2-2zm12 14H2V8h20v10z'/%3E%3C/svg%3E")!important;mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M10 4H2C.9 4 0 4.9 0 6v12c0 1.1.9 2 2 2h20c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2H12l-2-2zm12 14H2V8h20v10z'/%3E%3C/svg%3E")!important}
                .nav-item:nth-child(5) span{-webkit-mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M19.43 12.98c.04-.32.07-.65.07-.98s-.03-.66-.08-.98l2.11-1.65c.19-.15.24-.42.12-.64l-2-3.46c-.12-.22-.37-.31-.6-.22l-2.49 1c-.52-.4-1.08-.73-1.69-.98L14.5 2.42A.49.49 0 0 0 14 2h-4c-.25 0-.46.18-.5.42L9.12 5.07c-.61.25-1.17.59-1.69.98l-2.49-1a.49.49 0 0 0-.6.22l-2 3.46c-.13.22-.07.49.12.64l2.11 1.65c-.04.32-.08.66-.08.98s.03.66.08.98l-2.11 1.65a.49.49 0 0 0-.12.64l2 3.46c.12.22.37.31.6.22l2.49-1c.52.4 1.08.73 1.69.98l.38 2.65c.04.24.25.42.5.42h4c.25 0 .46-.18.5-.42l.38-2.65c.61-.25 1.17-.58 1.69-.98l2.49 1c.23.08.48 0 .6-.22l2-3.46a.49.49 0 0 0-.12-.64l-2.11-1.65zM12 15.5A3.5 3.5 0 1 1 12 8a3.5 3.5 0 0 1 0 7.5z'/%3E%3C/svg%3E")!important;mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M19.43 12.98c.04-.32.07-.65.07-.98s-.03-.66-.08-.98l2.11-1.65c.19-.15.24-.42.12-.64l-2-3.46c-.12-.22-.37-.31-.6-.22l-2.49 1c-.52-.4-1.08-.73-1.69-.98L14.5 2.42A.49.49 0 0 0 14 2h-4c-.25 0-.46.18-.5.42L9.12 5.07c-.61.25-1.17.59-1.69.98l-2.49-1a.49.49 0 0 0-.6.22l-2 3.46c-.13.22-.07.49.12.64l2.11 1.65c-.04.32-.08.66-.08.98s.03.66.08.98l-2.11 1.65a.49.49 0 0 0-.12.64l2 3.46c.12.22.37.31.6.22l2.49-1c.52.4 1.08.73 1.69.98l.38 2.65c.04.24.25.42.5.42h4c.25 0 .46-.18.5-.42l.38-2.65c.61-.25 1.17-.58 1.69-.98l2.49 1c.23.08.48 0 .6-.22l2-3.46a.49.49 0 0 0-.12-.64l-2.11-1.65zM12 15.5A3.5 3.5 0 1 1 12 8a3.5 3.5 0 0 1 0 7.5z'/%3E%3C/svg%3E")!important}
                body.light .bottom-nav{background:rgba(255,255,255,.98)!important;border-top-color:#d6dde8!important;box-shadow:0 -10px 28px rgba(15,23,42,.10)!important}
                body.light .nav-item{color:#5b6677!important}
                body.light .nav-item.active{color:#087f74!important;background:#d9f2ed!important}

                .em-help-overlay{position:fixed;inset:0;z-index:99999;background:rgba(2,6,14,.82);display:flex;align-items:center;justify-content:center;padding:18px;font-family:Roboto,Inter,system-ui,-apple-system,Segoe UI,sans-serif}
                .em-help-card{width:min(100%,560px);max-height:88vh;overflow:auto;background:#111827;color:#eef4fb;border:1px solid #334155;border-radius:26px;box-shadow:0 30px 80px rgba(0,0,0,.48);padding:22px}
                .em-help-top{display:flex;align-items:flex-start;justify-content:space-between;gap:16px}
                .em-help-kicker{margin:0 0 5px;color:#54d6c6;font-size:13px;font-weight:800;letter-spacing:.08em;text-transform:uppercase}
                .em-help-title{margin:0;font:800 26px/1.2 Roboto,Inter,system-ui,sans-serif}
                .em-help-close{width:44px;height:44px;flex:0 0 44px;border:0;border-radius:50%;background:#1e293b;color:#fff;font:700 24px/1 Roboto,sans-serif}
                .em-help-progress{height:6px;background:#263244;border-radius:999px;overflow:hidden;margin:20px 0}
                .em-help-progress span{display:block;height:100%;background:#54d6c6;border-radius:inherit;transition:width .2s ease}
                .em-help-body{font:400 16px/1.55 Roboto,Inter,system-ui,sans-serif;color:#cbd5e1}
                .em-help-body h4{margin:0 0 9px;color:#fff;font:800 21px/1.25 Roboto,Inter,system-ui,sans-serif}
                .em-help-body ul{margin:12px 0 0;padding-left:21px}.em-help-body li{margin:9px 0}
                .em-help-actions{display:flex;gap:10px;justify-content:flex-end;margin-top:22px}
                .em-help-actions button{min-height:48px;padding:0 18px;border-radius:16px;font:700 15px/1 Roboto,Inter,system-ui,sans-serif}
                .em-help-back{background:transparent;color:#e2e8f0;border:1px solid #475569}
                .em-help-next{background:#54d6c6;color:#06201d;border:0}
                .em-help-step{margin:12px 0 0;color:#94a3b8;font-size:13px;font-weight:700}
                #androidHelpPanel details{border:1px solid var(--line);border-radius:16px;padding:0 14px;margin-top:10px;background:var(--card2)}
                #androidHelpPanel summary{cursor:pointer;min-height:50px;display:flex;align-items:center;font-family:Roboto,Inter,system-ui,sans-serif;font-size:15px;font-weight:800;color:var(--text)}
                #androidHelpPanel details p{font-family:Roboto,Inter,system-ui,sans-serif;font-size:14px;line-height:1.55;color:var(--muted);margin:0 0 14px}
                #androidHelpPanel button,#androidAppPanel button{font-family:Roboto,Inter,system-ui,sans-serif!important;font-size:15px!important;min-height:50px!important}
                body.light .em-help-card{background:#fff;color:#0f172a;border-color:#cbd5e1}.body.light .em-help-body{color:#475569}
                body.light .em-help-body{color:#475569}.body.light .em-help-body h4{color:#0f172a}
                body.light .em-help-body h4{color:#0f172a}.body.light .em-help-close{background:#e2e8f0;color:#0f172a}
                body.light .em-help-close{background:#e2e8f0;color:#0f172a}.body.light .em-help-back{color:#0f172a;border-color:#94a3b8}
                body.light .em-help-back{color:#0f172a;border-color:#94a3b8}
                @media(max-width:360px){.bottom-nav{padding-left:4px!important;padding-right:4px!important;gap:2px!important}.nav-item small{font-size:12px!important}.em-help-card{padding:18px}.em-help-title{font-size:23px}}
              `;
              document.head.appendChild(style);

              var guideSteps=[
                {title:'Welcome to English Mastery',body:'<p>This app works completely on your phone. It automatically opens the extracted course folder, so you do not need Simple HTTP Server or an internet connection.</p><ul><li>Keep the extracted English Mastery folder on your phone.</li><li>Do not rename, move, or delete its internal files.</li><li>Your study progress saves automatically.</li></ul>'},
                {title:'Home: your daily plan',body:'<p>The Home screen creates today’s workload from your daily target, the next guided lesson, and any reviews that are due.</p><ul><li>Tap <b>Start</b> to begin today’s lesson.</li><li>Use <b>Edit daily target</b> to change available study time.</li><li>The lesson may be longer than today’s target; the app shows both values separately.</li></ul>'},
                {title:'Course: follow the guided order',body:'<p>The Course tab contains the complete guided learning path across your books and audio sets.</p><ul><li>Continue from the next incomplete lesson.</li><li>Complete lesson steps in order.</li><li>Your place is saved automatically when you leave the app.</li></ul>'},
                {title:'Progress: track learning and reviews',body:'<p>The Progress tab shows completed lessons, study time, streaks, and scheduled revision.</p><ul><li>Reviews return after spaced intervals.</li><li>Finishing due reviews keeps earlier material active.</li><li>Weekly progress helps you adjust workload without losing your place.</li></ul>'},
                {title:'Library: books and audio',body:'<p>The Library tab contains the integrated PDFs and 947 audio tracks.</p><ul><li>Select a source or search for a unit or track.</li><li>Tap a track card before using the audio controls.</li><li>PDFs and audio remain available offline as long as the extracted folder stays on your phone.</li></ul>'},
                {title:'Settings, backup, and help',body:'<p>Settings contains theme controls, progress backup and restore, Android folder controls, and this Help Center.</p><ul><li>Export a backup before reinstalling or clearing app data.</li><li>Use <b>Change course folder</b> only when the folder has moved.</li><li>Use <b>Reload course</b> when a screen does not update.</li><li>You can replay this guide at any time from Settings.</li></ul>'}
              ];
              var guideIndex=0;

              function closeGuide(markComplete){
                var overlay=document.getElementById('englishMasteryGuide');
                if(overlay)overlay.remove();
                if(markComplete)localStorage.setItem('englishMasteryAndroidGuide','1.0.3');
              }

              function renderGuide(){
                var overlay=document.getElementById('englishMasteryGuide');
                if(!overlay)return;
                var step=guideSteps[guideIndex];
                overlay.querySelector('.em-help-title').textContent=step.title;
                overlay.querySelector('.em-help-body').innerHTML=step.body;
                overlay.querySelector('.em-help-progress span').style.width=((guideIndex+1)/guideSteps.length*100)+'%';
                overlay.querySelector('.em-help-step').textContent='Step '+(guideIndex+1)+' of '+guideSteps.length;
                overlay.querySelector('.em-help-back').style.visibility=guideIndex===0?'hidden':'visible';
                overlay.querySelector('.em-help-next').textContent=guideIndex===guideSteps.length-1?'Finish':'Next';
              }

              function openGuide(start){
                closeGuide(false);
                guideIndex=Math.max(0,Math.min(Number(start)||0,guideSteps.length-1));
                var overlay=document.createElement('div');
                overlay.className='em-help-overlay';
                overlay.id='englishMasteryGuide';
                overlay.setAttribute('role','dialog');
                overlay.setAttribute('aria-modal','true');
                overlay.innerHTML='<div class="em-help-card"><div class="em-help-top"><div><p class="em-help-kicker">User guide</p><h3 class="em-help-title"></h3></div><button class="em-help-close" aria-label="Close guide">×</button></div><div class="em-help-progress"><span></span></div><div class="em-help-body"></div><p class="em-help-step"></p><div class="em-help-actions"><button class="em-help-back">Back</button><button class="em-help-next">Next</button></div></div>';
                document.body.appendChild(overlay);
                overlay.querySelector('.em-help-close').onclick=function(){closeGuide(false)};
                overlay.querySelector('.em-help-back').onclick=function(){if(guideIndex>0){guideIndex--;renderGuide()}};
                overlay.querySelector('.em-help-next').onclick=function(){if(guideIndex<guideSteps.length-1){guideIndex++;renderGuide()}else{closeGuide(true)}};
                overlay.onclick=function(event){if(event.target===overlay)closeGuide(false)};
                renderGuide();
              }
              window.EnglishMasteryHelp={openGuide:openGuide,closeGuide:closeGuide};

              function forceAndroidStatus(){
                var network=document.getElementById('networkBadge');
                var storage=document.getElementById('storageBadge');
                if(network)network.textContent='OFFLINE APP';
                if(storage)storage.textContent='ON-DEVICE FILES';
              }

              function reconcileDailyPlan(){
                var settings=typeof getDailyPlanSettings==='function'?getDailyPlanSettings():null;
                var target=settings&&Number(settings.minutesPerDay);
                if(!target)return;
                var summary=document.querySelector('#dailyPlan .daily-plan-summary');
                if(summary&&summary.children[0]){
                  var value=summary.children[0].querySelector('b');
                  var label=summary.children[0].querySelector('span');
                  if(value)value.textContent=String(target);
                  if(label)label.textContent='daily target';
                }
                document.querySelectorAll('#dailyPlan .daily-task:not(.compact) p').forEach(function(p){
                  if(!p.dataset.totalMinutes){
                    var match=p.textContent.match(/^(Guided lesson\\s+\\d+)\\s+·\\s+about\\s+(\\d+)\\s+minutes$/);
                    if(match){p.dataset.lessonPrefix=match[1];p.dataset.totalMinutes=match[2]}
                  }
                  if(p.dataset.totalMinutes)p.textContent=p.dataset.lessonPrefix+' · '+p.dataset.totalMinutes+' min total · '+target+' min today';
                });
              }

              function addSettingsPanels(){
                var settingsScreen=document.querySelector('[data-screen="settings"]');
                if(!settingsScreen)return;
                if(!document.getElementById('androidHelpPanel')){
                  var help=document.createElement('section');
                  help.className='panel';
                  help.id='androidHelpPanel';
                  help.innerHTML='<div class="section-head"><div><p class="muted">Instructions and troubleshooting</p><h3>Help & User Guide</h3></div><span class="pill">New user</span></div><p class="muted">Learn what every tab does and solve common offline, audio, PDF, recording, backup, and folder problems.</p><div class="button-stack"><button class="primary" id="openUserGuideButton">Open guided walkthrough</button></div><details><summary>How do I start learning?</summary><p>Open Home and tap Start under Today’s new lesson. Follow each lesson step in order. Your place and progress save automatically.</p></details><details><summary>Why must I keep the extracted folder?</summary><p>The PDFs and audio are stored in that folder. The APK is the app interface and automatically reads those files. Do not move or delete the folder unless you select its new location in Settings.</p></details><details><summary>Audio or PDF is not opening</summary><p>Confirm the extracted folder still contains index.html, assets, audio, and PDF folders. Then use Reload course below. If the folder moved, choose it again.</p></details><details><summary>Speaking recorder is blocked</summary><p>Open Android Settings → Apps → English Mastery → Permissions and allow Microphone. Return to the lesson and try recording again.</p></details><details><summary>How do backup and restore work?</summary><p>Export progress from Settings before reinstalling or clearing app data. Keep the JSON backup somewhere safe. Use Restore progress and select that JSON file when needed.</p></details>';
                  settingsScreen.insertBefore(help,settingsScreen.firstChild);
                  document.getElementById('openUserGuideButton').onclick=function(){openGuide(0)};
                }
                if(!document.getElementById('androidAppPanel')){
                  var panel=document.createElement('section');
                  panel.className='panel';panel.id='androidAppPanel';
                  panel.innerHTML='<div class="section-head"><div><p class="muted">Native Android controls</p><h3>Android App</h3></div><span class="pill">v1.0.3</span></div><p class="muted">English Mastery runs fully on this device. No separate server app is required.</p><div class="button-stack"><button class="secondary" id="changeCourseFolderButton">Change course folder</button><button class="secondary" id="reloadAndroidCourseButton">Reload course</button></div>';
                  settingsScreen.appendChild(panel);
                  document.getElementById('changeCourseFolderButton').onclick=function(){AndroidBridge.chooseCourseFolder()};
                  document.getElementById('reloadAndroidCourseButton').onclick=function(){location.reload()};
                }
              }

              function hideInstallControls(){
                var button=document.getElementById('installAppButton');if(button)button.style.display='none';
                var status=document.getElementById('installStatus');if(status)status.textContent='Installed as the English Mastery Android app.';
              }

              var applying=false;
              function applyEnhancements(){
                if(applying)return;applying=true;
                try{forceAndroidStatus();reconcileDailyPlan();addSettingsPanels();hideInstallControls()}finally{applying=false}
              }
              applyEnhancements();
              setTimeout(applyEnhancements,250);
              setTimeout(applyEnhancements,900);
              setTimeout(function(){if(localStorage.getItem('englishMasteryAndroidGuide')!=='1.0.3')openGuide(0)},1100);
              new MutationObserver(function(){requestAnimationFrame(applyEnhancements)}).observe(document.documentElement,{subtree:true,childList:true,characterData:true,attributes:true,attributeFilter:['class']});
            })();
            """;
}
