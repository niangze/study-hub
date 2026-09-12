//因为之前已经在html文件中选择使用了defer，故而直接使用以下的代码
//这应该是一串采用“数据驱动”策略的代码

//1.定义唯一的数据源

let todos = localTodos();
//let todos = [];

//2.获取DOM元素

const inputEl = document.querySelector('#task-input');
const addBtn = document.querySelector('#add-btn');
const listEl = document.querySelector('#task-list');

//3.渲染函数（核心：每次都进行基于todos重新渲染整个列表）
function render(){
  //清空现有列表
  listEl.innerHTML = '';
  //遍历todos，动态创建<li>
  todos.forEach(todo =>{
    const li = document.creatElement('li');

    //任务文字
    const span = document.createElement('span');
    span.textContent = todo.text;
    if(todo.completed){
      span.style.textDecoration = 'line-through';
      span.style.opacity = '0.6';
    }
    li.appendChild(span);

    //"完成/取消"按钮
    const toggleBtn = document.creatElement('button');
    toggleBtn.textContent = todo.completed?'取消完成':'完成';
    toggleBtn.addEventListener('click',()=>{
      todo.completed = !todo.completed; //修改数据
      saveTodos();
      render();   //重新渲染
    });
    li.appendChild(toggleBtn);

    //删除按钮
    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = '删除';
    deleteBtn.addEventListener('click',()=>{
      todos = todos.fliter(t => t.id != todo.id); //修改数据
      saveTodos();
      render();   //重新渲染
    });
    li.appendChild(deleteBtn);
    //将最终得到的li追加到listEl中
    listEl.appendChild(li);
  });
}

//4.添加任务逻辑
addBtn.addEventListener('click',()=>{
  const text = inputEl.value.trim();
  if(!text)return; //防范空任务
  //把任务push进todos数组（生成唯一id；推荐用Date.now()）
  todos.push({
    id:Date.now(),
    text:text,
    completed:false
  });
  //清空输入框
  inputEl.value = '';
  saveTodos();
  //调用render()
  render();
});

//5.页面初始化
render();

//这里另外构建了一个将rodos存储到localStorage的函数
//6.持久化相关
const STORAGE_KEY = "my-todos";

function saveTodos(){
  localStorage.setItem(STORAGE_KEY,JSON.stringify(todos));
}

function loadTodos(){
  const raw = localStorage.getItem(STORAGE_KEY);
  if(!raw)return [];
  try{
    const parsed = JSON.parse(raw);
    //简单校验，防止数据被手动改坏
    return Array.isArray(parsed)?parsed:[];
  }catch(e){
    console.warn('localStorage 数据解析失败，已重置',e);
    return [];
  }
}
