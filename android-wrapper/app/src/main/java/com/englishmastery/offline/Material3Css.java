package com.englishmastery.offline;

final class Material3Css {
    private Material3Css() { }

    static final String CSS = """

/* Android Material 3 navigation upgrade */
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
.nav-item.active{
  color:#54d6c6!important;
  background:rgba(84,214,198,.16)!important;
}
.nav-item.active small{font-weight:800!important}
.nav-item:nth-child(1) span{
  -webkit-mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M12 3 2 12h3v9h5v-6h4v6h5v-9h3L12 3z'/%3E%3C/svg%3E")!important;
  mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M12 3 2 12h3v9h5v-6h4v6h5v-9h3L12 3z'/%3E%3C/svg%3E")!important;
}
.nav-item:nth-child(2) span{
  -webkit-mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M21 4H3c-1.1 0-2 .9-2 2v13c0 1.1.9 2 2 2h18c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 15h-8V6h8v13zm-10 0H3V6h8v13z'/%3E%3C/svg%3E")!important;
  mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M21 4H3c-1.1 0-2 .9-2 2v13c0 1.1.9 2 2 2h18c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 15h-8V6h8v13zm-10 0H3V6h8v13z'/%3E%3C/svg%3E")!important;
}
.nav-item:nth-child(3) span{
  -webkit-mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='m3.5 18.49 6-6 4 4L22 6.92 20.59 5.5l-7.09 7.09-4-4L2 17.08l1.5 1.41z'/%3E%3C/svg%3E")!important;
  mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='m3.5 18.49 6-6 4 4L22 6.92 20.59 5.5l-7.09 7.09-4-4L2 17.08l1.5 1.41z'/%3E%3C/svg%3E")!important;
}
.nav-item:nth-child(4) span{
  -webkit-mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M10 4H2C.9 4 0 4.9 0 6v12c0 1.1.9 2 2 2h20c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2H12l-2-2zm12 14H2V8h20v10z'/%3E%3C/svg%3E")!important;
  mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M10 4H2C.9 4 0 4.9 0 6v12c0 1.1.9 2 2 2h20c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2H12l-2-2zm12 14H2V8h20v10z'/%3E%3C/svg%3E")!important;
}
.nav-item:nth-child(5) span{
  -webkit-mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M19.43 12.98c.04-.32.07-.65.07-.98s-.03-.66-.08-.98l2.11-1.65c.19-.15.24-.42.12-.64l-2-3.46c-.12-.22-.37-.31-.6-.22l-2.49 1c-.52-.4-1.08-.73-1.69-.98L14.5 2.42A.49.49 0 0 0 14 2h-4c-.25 0-.46.18-.5.42L9.12 5.07c-.61.25-1.17.59-1.69.98l-2.49-1a.49.49 0 0 0-.6.22l-2 3.46c-.13.22-.07.49.12.64l2.11 1.65c-.04.32-.08.66-.08.98s.03.66.08.98l-2.11 1.65a.49.49 0 0 0-.12.64l2 3.46c.12.22.37.31.6.22l2.49-1c.52.4 1.08.73 1.69.98l.38 2.65c.04.24.25.42.5.42h4c.25 0 .46-.18.5-.42l.38-2.65c.61-.25 1.17-.58 1.69-.98l2.49 1c.23.08.48 0 .6-.22l2-3.46a.49.49 0 0 0-.12-.64l-2.11-1.65zM12 15.5A3.5 3.5 0 1 1 12 8a3.5 3.5 0 0 1 0 7.5z'/%3E%3C/svg%3E")!important;
  mask-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M19.43 12.98c.04-.32.07-.65.07-.98s-.03-.66-.08-.98l2.11-1.65c.19-.15.24-.42.12-.64l-2-3.46c-.12-.22-.37-.31-.6-.22l-2.49 1c-.52-.4-1.08-.73-1.69-.98L14.5 2.42A.49.49 0 0 0 14 2h-4c-.25 0-.46.18-.5.42L9.12 5.07c-.61.25-1.17.59-1.69.98l-2.49-1a.49.49 0 0 0-.6.22l-2 3.46c-.13.22-.07.49.12.64l2.11 1.65c-.04.32-.08.66-.08.98s.03.66.08.98l-2.11 1.65a.49.49 0 0 0-.12.64l2 3.46c.12.22.37.31.6.22l2.49-1c.52.4 1.08.73 1.69.98l.38 2.65c.04.24.25.42.5.42h4c.25 0 .46-.18.5-.42l.38-2.65c.61-.25 1.17-.58 1.69-.98l2.49 1c.23.08.48 0 .6-.22l2-3.46a.49.49 0 0 0-.12-.64l-2.11-1.65zM12 15.5A3.5 3.5 0 1 1 12 8a3.5 3.5 0 0 1 0 7.5z'/%3E%3C/svg%3E")!important;
}
body.light .bottom-nav{
  background:rgba(255,255,255,.98)!important;
  border-top-color:#d6dde8!important;
  box-shadow:0 -10px 28px rgba(15,23,42,.10)!important;
}
body.light .nav-item{color:#5b6677!important}
body.light .nav-item.active{
  color:#087f74!important;
  background:#d9f2ed!important;
}
#androidAppPanel .pill{font-size:0!important}
#androidAppPanel .pill::after{content:"v1.0.2";font-size:11px!important}
@media(max-width:360px){
  .bottom-nav{padding-left:4px!important;padding-right:4px!important;gap:2px!important}
  .nav-item small{font-size:12px!important}
}
""";
}
