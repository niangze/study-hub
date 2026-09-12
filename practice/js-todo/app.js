//因为之前已经在html文件中选择使用了defer，故而直接使用以下的代码

const btn = document.querySelector('#btn');

if(btn){
  btn.addEventListener('click',()=>{
    alert('the botton already get clicked');
  });
}
