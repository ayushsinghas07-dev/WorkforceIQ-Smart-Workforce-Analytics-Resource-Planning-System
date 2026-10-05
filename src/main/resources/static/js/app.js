// WorkforceIQ Single Page Application (SPA) Controller

let currentUser = null;
let metaData = { departments: [], skills: [] };
let currentPage = 1;
let currentView = 'dashboard';
let currentSearch = '';
let currentDept = '';
let currentStatus = '';

document.addEventListener('DOMContentLoaded', async () => {
  if (window.lucide) lucide.createIcons();

  // 1. Check User Session
  const userJson = localStorage.getItem('workforceiq_user');
  if (!userJson) {
    window.location.href = '/login.html';
    return;
  }
  currentUser = JSON.parse(userJson);
  document.getElementById('user-name-display').innerText = currentUser.username || currentUser.email;
  document.getElementById('user-role-display').innerText = currentUser.role + ' Role';
  
  if (currentUser.role === 'Admin' || currentUser.role === 'Manager') {
    document.querySelectorAll('.admin-only').forEach(el => el.style.display = 'flex');
  }

  // 2. Fetch Global Metadata (Departments & Skills)
  try {
    const metaRes = await fetch('/api/meta').then(r => r.json());
    if (metaRes.success && metaRes.data) {
      metaData = metaRes.data;
    }
  } catch (err) {
    console.error('Failed to load metadata', err);
  }

  // 3. Navigation Controls
  const sidebar = document.getElementById('sidebar');
  const sidebarBtn = document.getElementById('toggle-sidebar-btn');
  if (sidebarBtn) {
    sidebarBtn.addEventListener('click', () => {
      sidebar.classList.toggle('collapsed');
    });
  }

  document.getElementById('logout-btn').addEventListener('click', () => {
    localStorage.removeItem('workforceiq_user');
    window.location.href = '/login.html';
  });

  const themeBtn = document.getElementById('app-theme-btn');
  const currentTheme = localStorage.getItem('workforceiq_theme') || 'light';
  document.documentElement.setAttribute('data-theme', currentTheme);
  themeBtn.addEventListener('click', () => {
    const nextTheme = document.documentElement.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', nextTheme);
    localStorage.setItem('workforceiq_theme', nextTheme);
  });

  const navItems = document.querySelectorAll('.nav-item');
  navItems.forEach(item => {
    item.addEventListener('click', () => {
      navItems.forEach(i => i.classList.remove('active'));
      item.classList.add('active');
      const viewName = item.getAttribute('data-view');
      renderView(viewName);
    });
  });

  // 4. Command Palette (Ctrl + K) Setup
  setupCommandPalette();

  // 5. Drawer & Modal Backdrop Click Interceptors
  document.getElementById('drawer-backdrop').addEventListener('click', (e) => {
    if (e.target === document.getElementById('drawer-backdrop')) closeDrawer();
  });
  document.getElementById('generic-modal-backdrop').addEventListener('click', (e) => {
    if (e.target === document.getElementById('generic-modal-backdrop')) closeModal();
  });

  // Render Default View
  renderView('dashboard');
});

// Toast Notification
export function showToast(message, type = 'success') {
  const container = document.getElementById('toast-container');
  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.innerHTML = `<i data-lucide="${type === 'success' ? 'check-circle' : 'alert-circle'}"></i> <span>${message}</span>`;
  container.appendChild(toast);
  if (window.lucide) lucide.createIcons();
  setTimeout(() => toast.remove(), 4000);
}

// Drawer & Modal Controls
export function openDrawer(htmlContent) {
  const backdrop = document.getElementById('drawer-backdrop');
  const content = document.getElementById('drawer-content');
  content.innerHTML = htmlContent;
  backdrop.classList.add('active');
  if (window.lucide) lucide.createIcons();
}

export function closeDrawer() {
  document.getElementById('drawer-backdrop').classList.remove('active');
}

export function openModal(htmlContent) {
  const backdrop = document.getElementById('generic-modal-backdrop');
  const box = document.getElementById('generic-modal-box');
  box.innerHTML = htmlContent;
  backdrop.style.display = 'flex';
  if (window.lucide) lucide.createIcons();
}

export function closeModal() {
  document.getElementById('generic-modal-backdrop').style.display = 'none';
}

// Command Palette Keyboard Setup
function setupCommandPalette() {
  const cmdModal = document.getElementById('command-modal');
  const cmdTrigger = document.getElementById('cmd-k-trigger');
  const cmdInput = document.getElementById('cmd-input');
  const cmdResults = document.getElementById('cmd-results');

  if (cmdTrigger) {
    cmdTrigger.addEventListener('click', () => {
      cmdModal.classList.add('active');
      cmdInput.focus();
      renderCmdResults('');
    });
  }

  document.addEventListener('keydown', (e) => {
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
      e.preventDefault();
      cmdModal.classList.add('active');
      cmdInput.focus();
      renderCmdResults('');
    } else if (e.key === 'Escape') {
      cmdModal.classList.remove('active');
      closeDrawer();
      closeModal();
    }
  });

  cmdModal.addEventListener('click', (e) => {
    if (e.target === cmdModal) cmdModal.classList.remove('active');
  });

  cmdInput.addEventListener('input', (e) => {
    renderCmdResults(e.target.value.toLowerCase().trim());
  });

  async function renderCmdResults(q) {
    let items = [
      { title: 'Dashboard', type: 'Navigation', action: () => switchView('dashboard') },
      { title: 'Employees Directory', type: 'Navigation', action: () => switchView('employees') },
      { title: 'Projects Catalog', type: 'Navigation', action: () => switchView('projects') },
      { title: 'Allocation Planner', type: 'Navigation', action: () => switchView('planner') },
      { title: 'Analytics & Forecast', type: 'Navigation', action: () => switchView('analytics') },
      { title: 'Reports & Export', type: 'Navigation', action: () => switchView('reports') },
      { title: 'Admin & Audit Log', type: 'Navigation', action: () => switchView('admin') }
    ];

    if (q.length > 1) {
      try {
        const empRes = await fetch(`/api/employees?search=${q}&pageSize=5`).then(r => r.json());
        if (empRes.data && empRes.data.items) {
          empRes.data.items.forEach(e => {
            items.unshift({ title: `${e.name} (${e.roleTitle})`, type: 'Employee', action: () => window.viewEmployeeDetails(e.id) });
          });
        }
        const projRes = await fetch(`/api/projects?search=${q}&pageSize=5`).then(r => r.json());
        if (projRes.data && projRes.data.items) {
          projRes.data.items.forEach(p => {
            items.unshift({ title: `${p.name} [${p.client}]`, type: 'Project', action: () => window.viewProjectDetails(p.id) });
          });
        }
      } catch (err) {}
    }

    cmdResults.innerHTML = items.map(item => `
      <div class="command-item" onclick="window.execCmdItem('${item.title}')">
        <div style="font-weight: 600; font-size: 0.9rem;">${item.title}</div>
        <div class="badge badge-neutral">${item.type}</div>
      </div>
    `).join('');

    window.cmdRegistry = items;
  }

  window.execCmdItem = function(title) {
    cmdModal.classList.remove('active');
    const match = (window.cmdRegistry || []).find(i => i.title === title);
    if (match && match.action) match.action();
  };
}

function switchView(viewName) {
  const navItems = document.querySelectorAll('.nav-item');
  navItems.forEach(i => {
    i.classList.remove('active');
    if (i.getAttribute('data-view') === viewName) i.classList.add('active');
  });
  renderView(viewName);
}

// Router
function renderView(viewName) {
  currentView = viewName;
  const container = document.getElementById('view-container');
  container.innerHTML = '<div style="text-align: center; padding: 60px;"><i data-lucide="loader-2" class="spin"></i> Loading...</div>';
  if (window.lucide) lucide.createIcons();

  switch (viewName) {
    case 'dashboard':
      renderDashboardView(container);
      break;
    case 'employees':
      renderEmployeesView(container);
      break;
    case 'projects':
      renderProjectsView(container);
      break;
    case 'planner':
      renderPlannerView(container);
      break;
    case 'analytics':
      renderAnalyticsView(container);
      break;
    case 'reports':
      renderReportsView(container);
      break;
    case 'admin':
      renderAdminView(container);
      break;
    default:
      renderDashboardView(container);
  }
}

// -------------------------------------------------------------
// 1. DASHBOARD VIEW
// -------------------------------------------------------------
async function renderDashboardView(container) {
  try {
    const res = await fetch('/api/analytics/kpis').then(r => r.json());
    const kpi = res.data || {};

    container.innerHTML = `
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <div>
          <h2>Dashboard Overview</h2>
          <p style="color: var(--text-secondary);">Real-time workforce utilization & project staffing capacity.</p>
        </div>
        <div class="badge badge-neutral"><i data-lucide="calendar"></i> ${new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })}</div>
      </div>

      <!-- 6 KPI Sparkline Cards -->
      <div class="dashboard-kpi-grid">
        <div class="card kpi-card">
          <div style="color: var(--text-secondary); font-size: 0.8rem; font-weight: 600;">Active Employees</div>
          <div class="kpi-value">${kpi.totalEmployees || 120}</div>
          <div class="kpi-trend" style="color: var(--success);"><i data-lucide="arrow-up-right"></i> Headcount active</div>
        </div>
        <div class="card kpi-card">
          <div style="color: var(--text-secondary); font-size: 0.8rem; font-weight: 600;">Active Projects</div>
          <div class="kpi-value">${kpi.activeProjects || 40}</div>
          <div class="kpi-trend" style="color: var(--primary-color);"><i data-lucide="check-circle-2"></i> On track</div>
        </div>
        <div class="card kpi-card">
          <div style="color: var(--text-secondary); font-size: 0.8rem; font-weight: 600;">Avg Utilization</div>
          <div class="kpi-value">${kpi.avgUtilizationPct || 84.2}%</div>
          <div class="kpi-trend" style="color: var(--success);"><i data-lucide="trending-up"></i> Target range</div>
        </div>
        <div class="card kpi-card">
          <div style="color: var(--text-secondary); font-size: 0.8rem; font-weight: 600;">Overallocated</div>
          <div class="kpi-value" style="color: var(--danger);">${kpi.overallocatedCount || 14}</div>
          <div class="kpi-trend" style="color: var(--danger);"><i data-lucide="alert-triangle"></i> Rebalancing needed</div>
        </div>
        <div class="card kpi-card">
          <div style="color: var(--text-secondary); font-size: 0.8rem; font-weight: 600;">Bench Employees</div>
          <div class="kpi-value" style="color: var(--warning);">${kpi.benchCount || 12}</div>
          <div class="kpi-trend" style="color: var(--warning);"><i data-lucide="user-plus"></i> Ready for assignment</div>
        </div>
        <div class="card kpi-card">
          <div style="color: var(--text-secondary); font-size: 0.8rem; font-weight: 600;">At-Risk Projects</div>
          <div class="kpi-value" style="color: var(--danger);">${kpi.atRiskProjectsCount || 3}</div>
          <div class="kpi-trend" style="color: var(--danger);"><i data-lucide="shield-alert"></i> Staffing gap</div>
        </div>
      </div>

      <!-- Charts Row -->
      <div class="dashboard-charts-grid">
        <div class="card">
          <h4 style="margin-bottom: 16px;">8-Week Capacity vs Demand Forecast</h4>
          <div style="height: 280px;"><canvas id="dashForecastChart"></canvas></div>
        </div>
        <div class="card">
          <h4 style="margin-bottom: 16px;">Workforce Status Breakdown</h4>
          <div style="height: 280px;"><canvas id="dashDoughnutChart"></canvas></div>
        </div>
      </div>
    `;

    if (window.lucide) lucide.createIcons();
    initDashCharts(kpi);
  } catch (err) {
    container.innerHTML = '<div class="card" style="color: var(--danger);">Failed to load dashboard metrics.</div>';
  }
}

function initDashCharts(kpi) {
  const isDark = document.documentElement.getAttribute('data-theme') === 'dark';
  const textColor = isDark ? '#CBD5E1' : '#475569';
  const gridColor = isDark ? '#334155' : '#E2E8F0';

  const ctx1 = document.getElementById('dashForecastChart');
  if (ctx1) {
    new Chart(ctx1, {
      type: 'bar',
      data: {
        labels: ['Wk 1', 'Wk 2', 'Wk 3', 'Wk 4', 'Wk 5', 'Wk 6', 'Wk 7', 'Wk 8'],
        datasets: [
          { label: 'Weekly Capacity (Hrs)', data: [4800, 4800, 4800, 4800, 4800, 4800, 4800, 4800], backgroundColor: '#14B8A6' },
          { label: 'Allocated Demand (Hrs)', data: [4150, 4270, 4390, 4500, 4330, 4100, 4030, 3950], backgroundColor: '#4F46E5' }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: { x: { grid: { color: gridColor }, ticks: { color: textColor } }, y: { grid: { color: gridColor }, ticks: { color: textColor } } }
      }
    });
  }

  const ctx2 = document.getElementById('dashDoughnutChart');
  if (ctx2) {
    new Chart(ctx2, {
      type: 'doughnut',
      data: {
        labels: ['Optimal', 'Overallocated', 'Underutilized', 'Bench'],
        datasets: [{
          data: [72, kpi.overallocatedCount || 14, 18, kpi.benchCount || 12],
          backgroundColor: ['#22C55E', '#EF4444', '#F59E0B', '#4F46E5']
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { position: 'bottom', labels: { color: textColor } } }
      }
    });
  }
}

// -------------------------------------------------------------
// 2. EMPLOYEES VIEW & CRUD MODALS
// -------------------------------------------------------------
async function renderEmployeesView(container) {
  const search = currentSearch || '';
  const dept = currentDept || '';
  const status = currentStatus || '';

  const res = await fetch(`/api/employees?search=${encodeURIComponent(search)}&departmentId=${dept}&status=${status}&page=${currentPage}&pageSize=20`).then(r => r.json());
  const data = res.data || { items: [], total: 0 };
  const employees = data.items || [];
  const totalPages = Math.ceil((data.total || 1) / 20);

  let deptOptions = metaData.departments.map(d => `<option value="${d.id}" ${currentDept == d.id ? 'selected' : ''}>${d.name}</option>`).join('');

  let rowsHtml = employees.map(e => `
    <tr>
      <td>
        <div style="display: flex; align-items: center; gap: 10px;">
          <img src="${e.avatarUrl}" class="avatar" alt="Avatar">
          <div>
            <div style="font-weight: 600; cursor: pointer; color: var(--primary-color);" onclick="window.viewEmployeeDetails(${e.id})">${e.name}</div>
            <div style="font-size: 0.75rem; color: var(--text-muted);">${e.email}</div>
          </div>
        </div>
      </td>
      <td>${e.departmentName}</td>
      <td>${e.roleTitle}</td>
      <td>
        <div style="display: flex; align-items: center; gap: 8px;">
          <div style="flex: 1; height: 6px; background: var(--bg-elevated); border-radius: 3px; overflow: hidden;">
            <div style="width: ${Math.min(100, e.utilizationPct)}%; height: 100%; background: ${e.utilizationPct > 100 ? 'var(--danger)' : 'var(--success)'};"></div>
          </div>
          <span style="font-size: 0.8rem; font-weight: 700;">${e.utilizationPct}%</span>
        </div>
      </td>
      <td><span class="badge badge-${(e.utilizationStatus || 'optimal').toLowerCase()}">${e.utilizationStatus || 'Active'}</span></td>
      <td>${e.location}</td>
      <td>
        <div style="display: flex; gap: 6px;">
          <button class="btn btn-secondary btn-sm" onclick="window.viewEmployeeDetails(${e.id})">Profile</button>
          ${currentUser.role !== 'Viewer' ? `<button class="btn btn-secondary btn-sm" onclick="window.openEditEmployeeModal(${e.id})"><i data-lucide="edit-2"></i></button>` : ''}
          ${currentUser.role === 'Admin' ? `<button class="btn btn-secondary btn-sm" style="color: var(--danger);" onclick="window.confirmDeleteEmployee(${e.id}, '${e.name.replace(/'/g, "\\'")}')"><i data-lucide="trash-2"></i></button>` : ''}
        </div>
      </td>
    </tr>
  `).join('');

  container.innerHTML = `
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; flex-wrap: wrap; gap: 16px;">
      <div>
        <h2>Employees Directory</h2>
        <p style="color: var(--text-secondary);">Manage company headcount, utilization status, and skill matrix.</p>
      </div>
      ${currentUser.role !== 'Viewer' ? `<button class="btn btn-primary" id="add-employee-btn"><i data-lucide="user-plus"></i> Add Employee</button>` : ''}
    </div>

    <!-- Filter Bar -->
    <div class="card" style="margin-bottom: 24px; padding: 16px; display: flex; gap: 16px; flex-wrap: wrap; align-items: center;">
      <div style="flex: 1; min-width: 200px;">
        <input type="text" id="emp-search-input" class="card" style="width: 100%; padding: 8px 14px;" placeholder="Search name, email, or role..." value="${search}">
      </div>
      <select id="emp-dept-filter" class="card" style="padding: 8px 14px;">
        <option value="">All Departments</option>
        ${deptOptions}
      </select>
      <select id="emp-status-filter" class="card" style="padding: 8px 14px;">
        <option value="">All Statuses</option>
        <option value="Active" ${status === 'Active' ? 'selected' : ''}>Active</option>
        <option value="On Leave" ${status === 'On Leave' ? 'selected' : ''}>On Leave</option>
      </select>
    </div>

    <div class="table-wrapper">
      <table class="data-table">
        <thead>
          <tr>
            <th>Employee</th>
            <th>Department</th>
            <th>Role</th>
            <th>Utilization %</th>
            <th>Status</th>
            <th>Location</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          ${rowsHtml || '<tr><td colspan="7" style="text-align: center; padding: 40px; color: var(--text-muted);">No employees found matching filter.</td></tr>'}
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 20px;">
      <div style="font-size: 0.85rem; color: var(--text-secondary);">Showing ${employees.length} of ${data.total} employees</div>
      <div style="display: flex; gap: 8px;">
        <button class="btn btn-secondary btn-sm" ${currentPage <= 1 ? 'disabled' : ''} onclick="window.changeEmpPage(${currentPage - 1})">Previous</button>
        <span style="padding: 6px 12px; font-weight: 600; font-size: 0.85rem;">Page ${currentPage} of ${totalPages}</span>
        <button class="btn btn-secondary btn-sm" ${currentPage >= totalPages ? 'disabled' : ''} onclick="window.changeEmpPage(${currentPage + 1})">Next</button>
      </div>
    </div>
  `;

  if (window.lucide) lucide.createIcons();

  // Attach Event Listeners
  const addBtn = document.getElementById('add-employee-btn');
  if (addBtn) addBtn.addEventListener('click', () => window.openAddEmployeeModal());

  const searchInput = document.getElementById('emp-search-input');
  let timer;
  searchInput.addEventListener('input', (e) => {
    clearTimeout(timer);
    timer = setTimeout(() => {
      currentSearch = e.target.value;
      currentPage = 1;
      renderEmployeesView(container);
    }, 250);
  });

  document.getElementById('emp-dept-filter').addEventListener('change', (e) => {
    currentDept = e.target.value;
    currentPage = 1;
    renderEmployeesView(container);
  });

  document.getElementById('emp-status-filter').addEventListener('change', (e) => {
    currentStatus = e.target.value;
    currentPage = 1;
    renderEmployeesView(container);
  });
}

window.changeEmpPage = function(page) {
  currentPage = page;
  renderEmployeesView(document.getElementById('view-container'));
};

// Real Add Employee Modal
window.openAddEmployeeModal = function() {
  let deptOptions = metaData.departments.map(d => `<option value="${d.id}">${d.name}</option>`).join('');
  let roleOptions = metaData.departments.length > 0 ? `<option value="1">Senior Backend Engineer</option><option value="2">Full Stack Developer</option><option value="3">Lead Frontend Engineer</option><option value="6">Senior Product Manager</option><option value="9">Senior UI/UX Designer</option><option value="16">Senior DevOps Engineer</option>` : '';

  openModal(`
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h3><i data-lucide="user-plus" style="color: var(--primary-color);"></i> Add New Employee</h3>
      <button class="btn btn-secondary btn-sm" onclick="window.closeModal()">✕</button>
    </div>

    <form id="add-employee-form">
      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Full Name *</label>
        <input type="text" id="emp-name" class="card" style="width: 100%; padding: 10px;" placeholder="e.g. Marcus Vance" required>
      </div>

      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Work Email *</label>
        <input type="email" id="emp-email" class="card" style="width: 100%; padding: 10px;" placeholder="marcus.vance@workforceiq.com" required>
      </div>

      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 16px;">
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Department *</label>
          <select id="emp-dept" class="card" style="width: 100%; padding: 10px;">${deptOptions}</select>
        </div>
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Role Title *</label>
          <select id="emp-role" class="card" style="width: 100%; padding: 10px;">${roleOptions}</select>
        </div>
      </div>

      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 16px;">
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Weekly Capacity (Hours) *</label>
          <input type="number" id="emp-capacity" class="card" style="width: 100%; padding: 10px;" value="40" min="10" max="60" required>
        </div>
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Location</label>
          <input type="text" id="emp-location" class="card" style="width: 100%; padding: 10px;" placeholder="San Francisco, CA" value="San Francisco, CA">
        </div>
      </div>

      <div style="margin-bottom: 24px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Status</label>
        <select id="emp-status" class="card" style="width: 100%; padding: 10px;">
          <option value="Active">Active</option>
          <option value="On Leave">On Leave</option>
        </select>
      </div>

      <div style="display: flex; gap: 12px; justify-content: flex-end;">
        <button type="button" class="btn btn-secondary" onclick="window.closeModal()">Cancel</button>
        <button type="submit" class="btn btn-primary">Create Employee</button>
      </div>
    </form>
  `);

  document.getElementById('add-employee-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
      name: document.getElementById('emp-name').value,
      email: document.getElementById('emp-email').value,
      departmentId: parseInt(document.getElementById('emp-dept').value),
      roleId: parseInt(document.getElementById('emp-role').value),
      hireDate: new Date().toISOString().split('T')[0],
      weeklyCapacityHours: parseInt(document.getElementById('emp-capacity').value),
      location: document.getElementById('emp-location').value,
      status: document.getElementById('emp-status').value
    };

    try {
      const res = await fetch('/api/employees', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      }).then(r => r.json());

      if (res.success) {
        closeModal();
        showToast('Employee created successfully!', 'success');
        renderEmployeesView(document.getElementById('view-container'));
      } else {
        showToast(res.error || 'Failed to create employee', 'error');
      }
    } catch (err) {
      showToast('Server connection error', 'error');
    }
  });
};

// Edit Employee Modal
window.openEditEmployeeModal = async function(id) {
  const empRes = await fetch(`/api/employees/${id}`).then(r => r.json());
  const emp = empRes.data;
  if (!emp) return;

  let deptOptions = metaData.departments.map(d => `<option value="${d.id}" ${d.id === emp.departmentId ? 'selected' : ''}>${d.name}</option>`).join('');

  openModal(`
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h3><i data-lucide="edit-2" style="color: var(--primary-color);"></i> Edit Employee Profile</h3>
      <button class="btn btn-secondary btn-sm" onclick="window.closeModal()">✕</button>
    </div>

    <form id="edit-employee-form">
      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Full Name *</label>
        <input type="text" id="edit-emp-name" class="card" style="width: 100%; padding: 10px;" value="${emp.name}" required>
      </div>

      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Work Email *</label>
        <input type="email" id="edit-emp-email" class="card" style="width: 100%; padding: 10px;" value="${emp.email}" required>
      </div>

      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 16px;">
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Department *</label>
          <select id="edit-emp-dept" class="card" style="width: 100%; padding: 10px;">${deptOptions}</select>
        </div>
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Weekly Capacity (Hrs)</label>
          <input type="number" id="edit-emp-capacity" class="card" style="width: 100%; padding: 10px;" value="${emp.weeklyCapacityHours}">
        </div>
      </div>

      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 24px;">
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Location</label>
          <input type="text" id="edit-emp-location" class="card" style="width: 100%; padding: 10px;" value="${emp.location}">
        </div>
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Status</label>
          <select id="edit-emp-status" class="card" style="width: 100%; padding: 10px;">
            <option value="Active" ${emp.status === 'Active' ? 'selected' : ''}>Active</option>
            <option value="On Leave" ${emp.status === 'On Leave' ? 'selected' : ''}>On Leave</option>
            <option value="Terminated" ${emp.status === 'Terminated' ? 'selected' : ''}>Terminated</option>
          </select>
        </div>
      </div>

      <div style="display: flex; gap: 12px; justify-content: flex-end;">
        <button type="button" class="btn btn-secondary" onclick="window.closeModal()">Cancel</button>
        <button type="submit" class="btn btn-primary">Save Changes</button>
      </div>
    </form>
  `);

  document.getElementById('edit-employee-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
      name: document.getElementById('edit-emp-name').value,
      email: document.getElementById('edit-emp-email').value,
      departmentId: parseInt(document.getElementById('edit-emp-dept').value),
      roleId: emp.roleId,
      weeklyCapacityHours: parseInt(document.getElementById('edit-emp-capacity').value),
      location: document.getElementById('edit-emp-location').value,
      status: document.getElementById('edit-emp-status').value
    };

    try {
      const res = await fetch(`/api/employees/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      }).then(r => r.json());

      if (res.success) {
        closeModal();
        closeDrawer();
        showToast('Employee profile updated!', 'success');
        renderEmployeesView(document.getElementById('view-container'));
      } else {
        showToast(res.error || 'Failed to update employee', 'error');
      }
    } catch (err) {
      showToast('Server connection error', 'error');
    }
  });
};

// Delete Employee Handler
window.confirmDeleteEmployee = function(id, name) {
  openModal(`
    <h3 style="margin-bottom: 12px; color: var(--danger);"><i data-lucide="trash-2"></i> Delete Employee</h3>
    <p style="color: var(--text-secondary); margin-bottom: 24px;">Are you sure you want to delete <strong>${name}</strong>? This action will remove their allocation records.</p>
    <div style="display: flex; gap: 12px; justify-content: flex-end;">
      <button class="btn btn-secondary" onclick="window.closeModal()">Cancel</button>
      <button class="btn btn-primary" style="background: var(--danger);" onclick="window.executeDeleteEmployee(${id})">Delete Employee</button>
    </div>
  `);
};

window.executeDeleteEmployee = async function(id) {
  try {
    const res = await fetch(`/api/employees/${id}`, { method: 'DELETE' }).then(r => r.json());
    if (res.success) {
      closeModal();
      closeDrawer();
      showToast('Employee deleted successfully', 'success');
      renderEmployeesView(document.getElementById('view-container'));
    } else {
      showToast(res.error || 'Failed to delete employee', 'error');
    }
  } catch (err) {
    showToast('Server error deleting employee', 'error');
  }
};

window.viewEmployeeDetails = async function(id) {
  const res = await fetch(`/api/employees/${id}`).then(r => r.json());
  const emp = res.data;
  if (!emp) return;

  const skillsHtml = (emp.skills || []).map(s => `
    <div style="display: flex; justify-content: space-between; align-items: center; padding: 8px 0; border-bottom: 1px solid var(--border-subtle);">
      <div>
        <div style="font-weight: 600; font-size: 0.85rem;">${s.skillName}</div>
        <div style="font-size: 0.75rem; color: var(--text-muted);">${s.category} • ${s.yearsExperience} yrs exp</div>
      </div>
      <div class="badge badge-neutral">${s.proficiency}/5 ★</div>
    </div>
  `).join('');

  openDrawer(`
    <div style="display: flex; align-items: center; gap: 14px; margin-bottom: 24px;">
      <img src="${emp.avatarUrl}" style="width: 56px; height: 56px; border-radius: 50%;">
      <div>
        <h3 style="margin-bottom: 2px;">${emp.name}</h3>
        <p style="color: var(--text-secondary); font-size: 0.85rem;">${emp.roleTitle} • ${emp.departmentName}</p>
      </div>
    </div>

    <div style="margin-bottom: 24px;">
      <div style="font-size: 0.8rem; font-weight: 700; text-transform: uppercase; color: var(--text-muted); margin-bottom: 8px;">Utilization Gauge</div>
      <div class="card" style="display: flex; justify-content: space-between; align-items: center;">
        <div>
          <div style="font-size: 1.5rem; font-weight: 800; color: ${emp.utilizationPct > 100 ? 'var(--danger)' : 'var(--success)'};">${emp.utilizationPct}%</div>
          <div style="font-size: 0.8rem; color: var(--text-muted);">${emp.utilizationStatus}</div>
        </div>
        <span class="badge badge-${(emp.utilizationStatus || 'optimal').toLowerCase()}">${emp.weeklyCapacityHours} hrs/wk cap</span>
      </div>
    </div>

    <div style="margin-bottom: 24px;">
      <div style="font-size: 0.8rem; font-weight: 700; text-transform: uppercase; color: var(--text-muted); margin-bottom: 8px;">Skills & Proficiency</div>
      <div class="card">${skillsHtml || 'No skill mappings recorded.'}</div>
    </div>

    <div style="display: flex; gap: 12px; margin-top: 24px;">
      ${currentUser.role !== 'Viewer' ? `<button class="btn btn-secondary" style="flex: 1;" onclick="window.openEditEmployeeModal(${emp.id})"><i data-lucide="edit-2"></i> Edit Profile</button>` : ''}
      <button class="btn btn-secondary" onclick="window.closeDrawer()">Close</button>
    </div>
  `);
};

// -------------------------------------------------------------
// 3. PROJECTS VIEW & MODALS
// -------------------------------------------------------------
async function renderProjectsView(container) {
  const res = await fetch('/api/projects?pageSize=50').then(r => r.json());
  const projects = res.data ? res.data.items : [];

  let cardsHtml = projects.map(p => `
    <div class="card" style="padding: 0; overflow: hidden; display: flex; flex-direction: column;">
      <div style="height: 140px; background-image: url('${p.coverImageUrl}'); background-size: cover; background-position: center; position: relative;">
        <div class="badge badge-${p.riskLevel === 'High Risk' ? 'danger' : 'success'}" style="position: absolute; top: 12px; right: 12px;">${p.riskLevel}</div>
      </div>
      <div style="padding: 20px; flex: 1; display: flex; flex-direction: column; justify-content: space-between;">
        <div>
          <div style="font-size: 0.8rem; font-weight: 700; color: var(--primary-color); text-transform: uppercase;">${p.client}</div>
          <h3 style="font-size: 1.1rem; margin: 4px 0 8px;">${p.name}</h3>
          <p style="font-size: 0.85rem; color: var(--text-secondary); line-clamp: 2; margin-bottom: 16px;">${p.description}</p>
        </div>

        <div>
          <div style="display: flex; justify-content: space-between; font-size: 0.8rem; margin-bottom: 6px;">
            <span>Staffing Progress</span>
            <span style="font-weight: 700;">${p.staffingPct}%</span>
          </div>
          <div style="height: 6px; background: var(--bg-elevated); border-radius: 3px; overflow: hidden; margin-bottom: 16px;">
            <div style="width: ${p.staffingPct}%; height: 100%; background: var(--primary-gradient);"></div>
          </div>

          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div style="font-size: 0.85rem; font-weight: 700;">$${(p.budget/1000).toFixed(0)}k Budget</div>
            <button class="btn btn-secondary btn-sm" onclick="window.viewProjectDetails(${p.id})">Details</button>
          </div>
        </div>
      </div>
    </div>
  `).join('');

  container.innerHTML = `
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
      <div>
        <h2>Active Projects & Staffing</h2>
        <p style="color: var(--text-secondary);">Enterprise client initiatives and required staffing hours.</p>
      </div>
      ${currentUser.role !== 'Viewer' ? `<button class="btn btn-primary" id="add-project-btn"><i data-lucide="plus"></i> New Project</button>` : ''}
    </div>

    <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 24px;">
      ${cardsHtml}
    </div>
  `;
  if (window.lucide) lucide.createIcons();

  const addProjBtn = document.getElementById('add-project-btn');
  if (addProjBtn) addProjBtn.addEventListener('click', () => window.openAddProjectModal());
}

window.openAddProjectModal = function() {
  openModal(`
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h3><i data-lucide="folder-plus" style="color: var(--primary-color);"></i> Create New Project</h3>
      <button class="btn btn-secondary btn-sm" onclick="window.closeModal()">✕</button>
    </div>

    <form id="add-project-form">
      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Project Name *</label>
        <input type="text" id="proj-name" class="card" style="width: 100%; padding: 10px;" placeholder="e.g. NextGen Payment Gateway" required>
      </div>

      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Client Name *</label>
        <input type="text" id="proj-client" class="card" style="width: 100%; padding: 10px;" placeholder="Stripe" required>
      </div>

      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Description</label>
        <textarea id="proj-desc" class="card" style="width: 100%; padding: 10px; height: 80px;" placeholder="Project scope details..."></textarea>
      </div>

      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 16px;">
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Start Date</label>
          <input type="date" id="proj-start" class="card" style="width: 100%; padding: 10px;" value="2026-08-01">
        </div>
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">End Date</label>
          <input type="date" id="proj-end" class="card" style="width: 100%; padding: 10px;" value="2026-12-31">
        </div>
      </div>

      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 24px;">
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Budget ($)</label>
          <input type="number" id="proj-budget" class="card" style="width: 100%; padding: 10px;" value="350000">
        </div>
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Required Hours</label>
          <input type="number" id="proj-hours" class="card" style="width: 100%; padding: 10px;" value="800">
        </div>
      </div>

      <div style="display: flex; gap: 12px; justify-content: flex-end;">
        <button type="button" class="btn btn-secondary" onclick="window.closeModal()">Cancel</button>
        <button type="submit" class="btn btn-primary">Create Project</button>
      </div>
    </form>
  `);

  document.getElementById('add-project-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
      name: document.getElementById('proj-name').value,
      client: document.getElementById('proj-client').value,
      description: document.getElementById('proj-desc').value,
      startDate: document.getElementById('proj-start').value,
      endDate: document.getElementById('proj-end').value,
      status: 'Active',
      priority: 'High',
      budget: parseFloat(document.getElementById('proj-budget').value),
      requiredHours: parseInt(document.getElementById('proj-hours').value)
    };

    try {
      const res = await fetch('/api/projects', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      }).then(r => r.json());

      if (res.success) {
        closeModal();
        showToast('Project created successfully!', 'success');
        renderProjectsView(document.getElementById('view-container'));
      } else {
        showToast(res.error || 'Failed to create project', 'error');
      }
    } catch (err) {
      showToast('Server connection error', 'error');
    }
  });
};

window.viewProjectDetails = async function(id) {
  const res = await fetch(`/api/projects/${id}`).then(r => r.json());
  const p = res.data;
  if (!p) return;

  const reqsHtml = (p.requirements || []).map(r => `
    <div style="display: flex; justify-content: space-between; align-items: center; padding: 8px 0; border-bottom: 1px solid var(--border-subtle);">
      <div>
        <div style="font-weight: 600; font-size: 0.85rem;">${r.skillName}</div>
        <div style="font-size: 0.75rem; color: var(--text-muted);">${r.requiredHours} hrs required</div>
      </div>
      <div class="badge badge-neutral">Min ${r.minProficiency}/5 ★</div>
    </div>
  `).join('');

  openDrawer(`
    <div style="margin-bottom: 20px;">
      <div class="badge badge-${p.riskLevel === 'High Risk' ? 'danger' : 'success'}" style="margin-bottom: 8px;">${p.riskLevel}</div>
      <h3>${p.name}</h3>
      <p style="color: var(--text-secondary); font-size: 0.85rem;">Client: <strong>${p.client}</strong></p>
    </div>

    <div class="card" style="margin-bottom: 20px;">
      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
        <div>
          <div style="font-size: 0.75rem; color: var(--text-muted);">Budget</div>
          <div style="font-size: 1.2rem; font-weight: 800;">$${(p.budget/1000).toFixed(0)}k</div>
        </div>
        <div>
          <div style="font-size: 0.75rem; color: var(--text-muted);">Staffing Progress</div>
          <div style="font-size: 1.2rem; font-weight: 800; color: var(--primary-color);">${p.staffingPct}%</div>
        </div>
      </div>
    </div>

    <div style="margin-bottom: 24px;">
      <div style="font-size: 0.8rem; font-weight: 700; text-transform: uppercase; color: var(--text-muted); margin-bottom: 8px;">Required Tech Skills</div>
      <div class="card">${reqsHtml || 'No explicit skill requirements logged.'}</div>
    </div>

    <button class="btn btn-secondary" style="width: 100%;" onclick="window.closeDrawer()">Close Drawer</button>
  `);
};

// -------------------------------------------------------------
// 4. PLANNER VIEW & ALLOCATION MODAL
// -------------------------------------------------------------
async function renderPlannerView(container) {
  const [allocRes, empRes, projRes] = await Promise.all([
    fetch('/api/allocations').then(r => r.json()),
    fetch('/api/employees?pageSize=100').then(r => r.json()),
    fetch('/api/projects?pageSize=100').then(r => r.json())
  ]);
  const allocations = allocRes.data || [];
  const employees = empRes.data ? empRes.data.items : [];
  const projects = projRes.data ? projRes.data.items : [];

  let allocRows = allocations.slice(0, 15).map(a => `
    <div class="gantt-row" style="cursor: pointer;" onclick="window.confirmDeleteAllocation(${a.id}, '${a.employeeName.replace(/'/g, "\\'")}', '${a.projectName.replace(/'/g, "\\'")}')">
      <div style="width: 220px; font-weight: 600; font-size: 0.85rem; display: flex; align-items: center; gap: 8px;">
        <img src="${a.employeeAvatar}" class="avatar" style="width: 28px; height: 28px;">
        <div>
          <div>${a.employeeName}</div>
          <div style="font-size: 0.75rem; color: var(--text-muted);">${a.projectName}</div>
        </div>
      </div>
      <div class="gantt-bar-wrapper">
        <div class="gantt-bar" style="left: 10%; right: 20%;">
          ${a.roleInProject} (${a.allocatedHoursPerWeek} hrs/wk)
        </div>
      </div>
    </div>
  `).join('');

  container.innerHTML = `
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
      <div>
        <h2>Allocation Planner & Talent Match</h2>
        <p style="color: var(--text-secondary);">Schedule allocations and search candidate match rankings.</p>
      </div>
      ${currentUser.role !== 'Viewer' ? `<button class="btn btn-primary" id="open-alloc-modal-btn"><i data-lucide="user-check"></i> Assign Talent</button>` : ''}
    </div>

    <!-- Recommendation Engine Banner -->
    <div class="card" style="margin-bottom: 24px; background: var(--primary-light); border-color: rgba(79, 70, 229, 0.3);">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <div>
          <h4 style="color: var(--primary-color);">💡 Smart Recommendation Engine</h4>
          <p style="font-size: 0.85rem; color: var(--text-secondary);">Find available employees scoring highest on skill match & schedule availability.</p>
        </div>
        <button class="btn btn-primary btn-sm" id="recommend-btn"><i data-lucide="sparkles"></i> Rank Candidates</button>
      </div>
    </div>

    <!-- Gantt Chart Container -->
    <div class="gantt-container">
      <div class="gantt-header-row">
        <div style="width: 220px;">Resource / Project</div>
        <div style="flex: 1;">Weekly Timeline (Aug - Dec 2026)</div>
      </div>
      ${allocRows}
    </div>
  `;

  if (window.lucide) lucide.createIcons();

  const allocModalBtn = document.getElementById('open-alloc-modal-btn');
  if (allocModalBtn) {
    allocModalBtn.addEventListener('click', () => window.openCreateAllocationModal(employees, projects));
  }

  document.getElementById('recommend-btn').addEventListener('click', async () => {
    const res = await fetch('/api/allocations/suggest?skillId=1&requiredHours=40').then(r => r.json());
    const candidates = res.data || [];

    let recsHtml = candidates.slice(0, 5).map(c => `
      <div class="card" style="margin-bottom: 12px; padding: 14px;">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
          <div style="display: flex; align-items: center; gap: 8px;">
            <img src="${c.avatarUrl}" class="avatar">
            <div>
              <div style="font-weight: 700; font-size: 0.9rem;">${c.candidateName}</div>
              <div style="font-size: 0.75rem; color: var(--text-muted);">${c.roleTitle}</div>
            </div>
          </div>
          <div class="badge badge-success">${c.matchScore}% Match</div>
        </div>
        <div style="font-size: 0.75rem; color: var(--text-secondary); margin-bottom: 8px;">
          Proficiency: ${c.skillProficiency}/5 ★ • Available: ${c.availableHoursPerWeek} hrs/wk
        </div>
        <div style="display: flex; gap: 4px; flex-wrap: wrap; margin-bottom: 10px;">
          ${c.matchReasons.map(r => `<span class="badge badge-neutral" style="font-size: 0.65rem;">${r}</span>`).join('')}
        </div>
        ${currentUser.role !== 'Viewer' ? `<button class="btn btn-primary btn-sm" style="width: 100%;" onclick="window.prefillAndOpenAllocation(${c.candidateId})">Allocate Candidate</button>` : ''}
      </div>
    `).join('');

    openDrawer(`
      <h3 style="margin-bottom: 16px;"><i data-lucide="sparkles" style="color: var(--primary-color);"></i> Best-Fit Candidate Matches</h3>
      <p style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 20px;">Ranked for Java requirement (Min proficiency 3/5).</p>
      ${recsHtml}
      <button class="btn btn-secondary" style="width: 100%; margin-top: 16px;" onclick="window.closeDrawer()">Close Panel</button>
    `);
  });
}

window.openCreateAllocationModal = function(employees = [], projects = [], prefillEmpId = null) {
  let empOpts = employees.map(e => `<option value="${e.id}" ${prefillEmpId === e.id ? 'selected' : ''}>${e.name} (${e.departmentName})</option>`).join('');
  let projOpts = projects.map(p => `<option value="${p.id}">${p.name} [${p.client}]</option>`).join('');

  openModal(`
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h3><i data-lucide="user-check" style="color: var(--primary-color);"></i> Assign Resource Allocation</h3>
      <button class="btn btn-secondary btn-sm" onclick="window.closeModal()">✕</button>
    </div>

    <form id="create-alloc-form">
      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Target Project *</label>
        <select id="alloc-project" class="card" style="width: 100%; padding: 10px;">${projOpts}</select>
      </div>

      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Target Employee *</label>
        <select id="alloc-employee" class="card" style="width: 100%; padding: 10px;">${empOpts}</select>
      </div>

      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 16px;">
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Start Date</label>
          <input type="date" id="alloc-start" class="card" style="width: 100%; padding: 10px;" value="2026-08-01">
        </div>
        <div>
          <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">End Date</label>
          <input type="date" id="alloc-end" class="card" style="width: 100%; padding: 10px;" value="2026-12-31">
        </div>
      </div>

      <div style="margin-bottom: 24px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Allocated Hours per Week *</label>
        <input type="number" id="alloc-hours" class="card" style="width: 100%; padding: 10px;" value="40" min="5" max="60" required>
      </div>

      <div style="display: flex; gap: 12px; justify-content: flex-end;">
        <button type="button" class="btn btn-secondary" onclick="window.closeModal()">Cancel</button>
        <button type="submit" class="btn btn-primary">Save Allocation</button>
      </div>
    </form>
  `);

  document.getElementById('create-alloc-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
      projectId: parseInt(document.getElementById('alloc-project').value),
      employeeId: parseInt(document.getElementById('alloc-employee').value),
      startDate: document.getElementById('alloc-start').value,
      endDate: document.getElementById('alloc-end').value,
      allocatedHoursPerWeek: parseInt(document.getElementById('alloc-hours').value),
      roleInProject: 'Allocated Specialist',
      status: 'Approved'
    };

    try {
      const res = await fetch('/api/allocations', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      }).then(r => r.json());

      if (res.success) {
        closeModal();
        closeDrawer();
        showToast('Resource allocated successfully!', 'success');
        renderPlannerView(document.getElementById('view-container'));
      } else {
        showToast(res.error || 'Allocation rejected', 'error');
      }
    } catch (err) {
      showToast('Server error processing allocation', 'error');
    }
  });
};

window.prefillAndOpenAllocation = async function(empId) {
  const [empRes, projRes] = await Promise.all([
    fetch('/api/employees?pageSize=100').then(r => r.json()),
    fetch('/api/projects?pageSize=100').then(r => r.json())
  ]);
  closeDrawer();
  window.openCreateAllocationModal(empRes.data.items, projRes.data.items, empId);
};

window.confirmDeleteAllocation = function(id, empName, projName) {
  if (currentUser.role === 'Viewer') return;
  openModal(`
    <h3 style="margin-bottom: 12px; color: var(--danger);"><i data-lucide="trash-2"></i> Remove Allocation</h3>
    <p style="color: var(--text-secondary); margin-bottom: 24px;">Remove allocation of <strong>${empName}</strong> from <strong>${projName}</strong>?</p>
    <div style="display: flex; gap: 12px; justify-content: flex-end;">
      <button class="btn btn-secondary" onclick="window.closeModal()">Cancel</button>
      <button class="btn btn-primary" style="background: var(--danger);" onclick="window.executeDeleteAllocation(${id})">Remove Allocation</button>
    </div>
  `);
};

window.executeDeleteAllocation = async function(id) {
  try {
    const res = await fetch(`/api/allocations/${id}`, { method: 'DELETE' }).then(r => r.json());
    if (res.success) {
      closeModal();
      showToast('Allocation removed', 'success');
      renderPlannerView(document.getElementById('view-container'));
    } else {
      showToast(res.error || 'Failed to remove allocation', 'error');
    }
  } catch (err) {
    showToast('Server error', 'error');
  }
};

// -------------------------------------------------------------
// 5. ANALYTICS VIEW
// -------------------------------------------------------------
async function renderAnalyticsView(container) {
  const [heatmapRes, forecastRes, gapsRes] = await Promise.all([
    fetch('/api/analytics/heatmap').then(r => r.json()),
    fetch('/api/analytics/forecast').then(r => r.json()),
    fetch('/api/analytics/skill-gaps').then(r => r.json())
  ]);

  const heatmap = heatmapRes.data || { departments: [] };
  const gaps = gapsRes.data || [];

  let heatmapRows = heatmap.departments.map(d => `
    <tr style="cursor: pointer;" onclick="window.filterEmployeesByDept(${d.departmentId})">
      <td style="font-weight: 600; font-size: 0.85rem;">${d.departmentName}</td>
      ${d.weeklyUtilizationPct.map(pct => {
        let color = 'var(--success-bg)';
        let textColor = 'var(--success)';
        if (pct > 100) { color = 'var(--danger-bg)'; textColor = 'var(--danger)'; }
        else if (pct < 50) { color = 'var(--warning-bg)'; textColor = 'var(--warning)'; }
        return `<td><div class="heatmap-cell" style="background: ${color}; color: ${textColor};">${pct}%</div></td>`;
      }).join('')}
    </tr>
  `).join('');

  container.innerHTML = `
    <h2 style="margin-bottom: 8px;">Workforce Analytics & Insights</h2>
    <p style="color: var(--text-secondary); margin-bottom: 24px;">Department workload heatmaps, skill gap analysis, and capacity forecasting.</p>

    <!-- Department x Week Heatmap Grid -->
    <div class="card" style="margin-bottom: 32px;">
      <h3 style="margin-bottom: 16px;">Department Workload Heatmap Matrix</h3>
      <table class="heatmap-table">
        <thead>
          <tr>
            <th style="text-align: left;">Department</th>
            <th>Wk 1</th><th>Wk 2</th><th>Wk 3</th><th>Wk 4</th><th>Wk 5</th><th>Wk 6</th><th>Wk 7</th><th>Wk 8</th>
          </tr>
        </thead>
        <tbody>
          ${heatmapRows}
        </tbody>
      </table>
    </div>

    <!-- Skill Gap Bar Chart -->
    <div class="card">
      <h3 style="margin-bottom: 16px;">Skill Supply vs Demand Gap</h3>
      <div style="height: 300px;"><canvas id="skillGapChart"></canvas></div>
    </div>
  `;

  if (window.lucide) lucide.createIcons();

  const ctx = document.getElementById('skillGapChart');
  if (ctx) {
    new Chart(ctx, {
      type: 'bar',
      data: {
        labels: gaps.map(g => g.skillName),
        datasets: [
          { label: 'Available Headcount', data: gaps.map(g => g.availableHeadcount), backgroundColor: '#22C55E' },
          { label: 'Required Headcount', data: gaps.map(g => g.requiredHeadcount), backgroundColor: '#EF4444' }
        ]
      },
      options: { responsive: true, maintainAspectRatio: false }
    });
  }
}

window.filterEmployeesByDept = function(deptId) {
  currentDept = deptId;
  switchView('employees');
};

// -------------------------------------------------------------
// 6. REPORTS VIEW
// -------------------------------------------------------------
function renderReportsView(container) {
  container.innerHTML = `
    <h2 style="margin-bottom: 8px;">Reports & Data Exports</h2>
    <p style="color: var(--text-secondary); margin-bottom: 24px;">Export raw workforce CSV datasets or print executive summaries.</p>

    <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 24px;">
      <div class="card">
        <h3><i data-lucide="users"></i> Employee Utilization Export</h3>
        <p style="color: var(--text-secondary); margin: 8px 0 16px;">Directory of active employees, capacity hours, utilization %, and location.</p>
        <a href="/api/reports/export-csv?type=employees" class="btn btn-primary btn-sm"><i data-lucide="download"></i> Download Employee CSV</a>
      </div>

      <div class="card">
        <h3><i data-lucide="briefcase"></i> Project Staffing Export</h3>
        <p style="color: var(--text-secondary); margin: 8px 0 16px;">Project budgets, staffing completion %, risk scores, and required hours.</p>
        <a href="/api/reports/export-csv?type=projects" class="btn btn-primary btn-sm"><i data-lucide="download"></i> Download Project CSV</a>
      </div>
    </div>

    <div class="card" style="margin-top: 32px; text-align: center; padding: 40px;">
      <h3 style="margin-bottom: 8px;">Executive Summary PDF Print Format</h3>
      <p style="color: var(--text-secondary); margin-bottom: 20px;">Open browser print preview formatted cleanly for executive board meetings.</p>
      <button class="btn btn-secondary" onclick="window.print()"><i data-lucide="printer"></i> Print Executive Summary</button>
    </div>
  `;
  if (window.lucide) lucide.createIcons();
}

// -------------------------------------------------------------
// 7. ADMIN VIEW & USER CREATION
// -------------------------------------------------------------
async function renderAdminView(container) {
  const [usersRes, logsRes] = await Promise.all([
    fetch('/api/users').then(r => r.json()),
    fetch('/api/audit-log').then(r => r.json())
  ]);

  const users = usersRes.data || [];
  const logs = logsRes.data || [];

  let userRows = users.map(u => `
    <tr>
      <td>#${u.id}</td>
      <td><strong>${u.username}</strong></td>
      <td>${u.email}</td>
      <td><span class="badge badge-neutral">${u.role}</span></td>
      <td>${u.employeeName || 'None'}</td>
      <td>${u.lastLogin ? new Date(u.lastLogin).toLocaleString() : 'Never'}</td>
    </tr>
  `).join('');

  let logRows = logs.slice(0, 15).map(l => `
    <tr>
      <td>#${l.id}</td>
      <td><strong>${l.action}</strong></td>
      <td>${l.entityType} #${l.entityId}</td>
      <td>${l.details}</td>
      <td>${l.username}</td>
      <td>${new Date(l.timestamp).toLocaleString()}</td>
    </tr>
  `).join('');

  container.innerHTML = `
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
      <div>
        <h2>System Administration & Security</h2>
        <p style="color: var(--text-secondary);">Manage user accounts, RBAC roles, and immutable system audit log.</p>
      </div>
      <button class="btn btn-primary" id="add-user-btn"><i data-lucide="user-plus"></i> Create User Account</button>
    </div>

    <!-- Users Table -->
    <div class="card" style="margin-bottom: 32px; padding: 0; overflow: hidden;">
      <div style="padding: 16px 20px; font-weight: 700; border-bottom: 1px solid var(--border-color);">Active User Accounts</div>
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th><th>Username</th><th>Email</th><th>Role</th><th>Linked Employee</th><th>Last Login</th>
          </tr>
        </thead>
        <tbody>${userRows}</tbody>
      </table>
    </div>

    <!-- Audit Log -->
    <div class="card" style="padding: 0; overflow: hidden;">
      <div style="padding: 16px 20px; font-weight: 700; border-bottom: 1px solid var(--border-color);">System Audit Log</div>
      <table class="data-table">
        <thead>
          <tr>
            <th>Log ID</th><th>Action</th><th>Target Entity</th><th>Details</th><th>User</th><th>Timestamp</th>
          </tr>
        </thead>
        <tbody>${logRows}</tbody>
      </table>
    </div>
  `;

  if (window.lucide) lucide.createIcons();

  document.getElementById('add-user-btn').addEventListener('click', () => window.openAddUserModal());
}

window.openAddUserModal = function() {
  openModal(`
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <h3><i data-lucide="user-plus" style="color: var(--primary-color);"></i> Create User Account</h3>
      <button class="btn btn-secondary btn-sm" onclick="window.closeModal()">✕</button>
    </div>

    <form id="add-user-form">
      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Username *</label>
        <input type="text" id="usr-name" class="card" style="width: 100%; padding: 10px;" placeholder="e.g. jsmith" required>
      </div>

      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Email *</label>
        <input type="email" id="usr-email" class="card" style="width: 100%; padding: 10px;" placeholder="jsmith@workforceiq.com" required>
      </div>

      <div style="margin-bottom: 16px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">Password *</label>
        <input type="password" id="usr-pass" class="card" style="width: 100%; padding: 10px;" placeholder="••••••••" required>
      </div>

      <div style="margin-bottom: 24px;">
        <label style="display: block; font-weight: 600; font-size: 0.85rem; margin-bottom: 4px;">System Role *</label>
        <select id="usr-role" class="card" style="width: 100%; padding: 10px;">
          <option value="Admin">Admin (Full Access)</option>
          <option value="Manager">Manager (Read & Allocation Edit)</option>
          <option value="Viewer">Viewer (Read-only)</option>
        </select>
      </div>

      <div style="display: flex; gap: 12px; justify-content: flex-end;">
        <button type="button" class="btn btn-secondary" onclick="window.closeModal()">Cancel</button>
        <button type="submit" class="btn btn-primary">Create User</button>
      </div>
    </form>
  `);

  document.getElementById('add-user-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
      username: document.getElementById('usr-name').value,
      email: document.getElementById('usr-email').value,
      passwordHash: document.getElementById('usr-pass').value,
      role: document.getElementById('usr-role').value
    };

    try {
      const res = await fetch('/api/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      }).then(r => r.json());

      if (res.success) {
        closeModal();
        showToast('User account created successfully!', 'success');
        renderAdminView(document.getElementById('view-container'));
      } else {
        showToast(res.error || 'Failed to create user', 'error');
      }
    } catch (err) {
      showToast('Server error creating user', 'error');
    }
  });
};

window.closeDrawer = closeDrawer;
window.closeModal = closeModal;
