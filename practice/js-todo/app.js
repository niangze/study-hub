// 1. 持久化相关（提前定义，方便调用）
const STORAGE_KEY = "my-todos";

function saveTodos() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(todos));
}

function loadTodos() {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) return [];
  try {
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? parsed : [];
  } catch (e) {
    console.warn('localStorage 数据解析失败，已重置', e);
    return [];
  }
}

// 2. 定义唯一的数据源
let todos = loadTodos(); // 修正了函数名

// 3. 获取DOM元素
const inputEl = document.querySelector('#task-input');
const addBtn = document.querySelector('#add-btn');
const listEl = document.querySelector('#task-list');

// 4. 渲染函数
function render() {
  listEl.innerHTML = '';
  todos.forEach(todo => {
    const li = document.createElement('li'); // 修正了拼写

    const span = document.createElement('span');
    span.textContent = todo.text;
    if (todo.completed) {
      span.style.textDecoration = 'line-through';
      span.style.opacity = '0.6';
    }
    li.appendChild(span);

    const toggleBtn = document.createElement('button'); // 修正了拼写
    toggleBtn.textContent = todo.completed ? '取消完成' : '完成';
    toggleBtn.addEventListener('click', () => {
      todo.completed = !todo.completed;
      saveTodos();
      render();
    });
    li.appendChild(toggleBtn);

    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = '删除';
    deleteBtn.addEventListener('click', () => {
      todos = todos.filter(t => t.id !== todo.id); // 修正了拼写和严格相等
      saveTodos();
      render();
    });
    li.appendChild(deleteBtn);
    listEl.appendChild(li);
  });
}

// 5. 添加任务逻辑
addBtn.addEventListener('click', () => {
  const text = inputEl.value.trim();
  if (!text) return;
  todos.push({
    id: Date.now(),
    text: text,
    completed: false
  });
  inputEl.value = '';
  saveTodos();
  render();
});

// 6. 页面初始化
render();
