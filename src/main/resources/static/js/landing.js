// WorkforceIQ Landing Page Logic

document.addEventListener('DOMContentLoaded', () => {
  if (window.lucide) lucide.createIcons();

  // Theme Toggle with localStorage persistence
  const themeBtn = document.getElementById('theme-toggle-btn');
  const currentTheme = localStorage.getItem('workforceiq_theme') || 'light';
  document.documentElement.setAttribute('data-theme', currentTheme);
  updateThemeIcon(currentTheme);

  if (themeBtn) {
    themeBtn.addEventListener('click', () => {
      const nextTheme = document.documentElement.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
      document.documentElement.setAttribute('data-theme', nextTheme);
      localStorage.setItem('workforceiq_theme', nextTheme);
      updateThemeIcon(nextTheme);
      renderHeroChart();
      renderBenchmarkChart();
    });
  }

  function updateThemeIcon(theme) {
    if (!themeBtn) return;
    themeBtn.innerHTML = theme === 'dark' ? '<i data-lucide="sun"></i>' : '<i data-lucide="moon"></i>';
    if (window.lucide) lucide.createIcons();
  }

  // Fetch Live Stats from Backend API
  fetch('/api/analytics/kpis')
    .then(r => r.json())
    .then(res => {
      if (res.success && res.data) {
        const d = res.data;
        document.getElementById('stat-employees').innerText = d.totalEmployees || 120;
        document.getElementById('stat-projects').innerText = d.activeProjects || 40;
        document.getElementById('stat-utilization').innerText = (d.avgUtilizationPct || 84.2) + '%';
        document.getElementById('stat-allocations').innerText = '600+';
      }
    })
    .catch(err => console.log('Using default stat baseline numbers'));

  // Render Hero Mini Dashboard Preview Chart
  renderHeroChart();

  // Render Impact Benchmark Comparison Chart
  renderBenchmarkChart();
});

let heroChartInstance = null;
function renderHeroChart() {
  const ctx = document.getElementById('heroChart');
  if (!ctx) return;
  if (heroChartInstance) heroChartInstance.destroy();

  const isDark = document.documentElement.getAttribute('data-theme') === 'dark';
  const textColor = isDark ? '#CBD5E1' : '#475569';
  const gridColor = isDark ? '#334155' : '#E2E8F0';

  heroChartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4', 'Week 5', 'Week 6', 'Week 7', 'Week 8'],
      datasets: [
        {
          label: 'Workforce Capacity (Hrs)',
          data: [4800, 4800, 4800, 4800, 4800, 4800, 4800, 4800],
          borderColor: '#14B8A6',
          borderDash: [5, 5],
          borderWidth: 2,
          pointRadius: 0,
          fill: false
        },
        {
          label: 'Allocated Demand (Hrs)',
          data: [4150, 4270, 4390, 4500, 4330, 4100, 4030, 3950],
          borderColor: '#4F46E5',
          backgroundColor: 'rgba(79, 70, 229, 0.15)',
          borderWidth: 3,
          fill: true,
          tension: 0.4
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { labels: { color: textColor, font: { family: 'Plus Jakarta Sans', weight: '600' } } }
      },
      scales: {
        x: { grid: { color: gridColor }, ticks: { color: textColor } },
        y: { grid: { color: gridColor }, ticks: { color: textColor } }
      }
    }
  });
}

let benchmarkChartInstance = null;
function renderBenchmarkChart() {
  const ctx = document.getElementById('benchmarkChart');
  if (!ctx) return;
  if (benchmarkChartInstance) benchmarkChartInstance.destroy();

  const isDark = document.documentElement.getAttribute('data-theme') === 'dark';
  const textColor = isDark ? '#CBD5E1' : '#475569';
  const gridColor = isDark ? '#334155' : '#E2E8F0';

  benchmarkChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: [
        'Workforce Utilization %',
        'Find Best-fit Engineers',
        'Leave & Overlap Checks',
        '8-Wk Forecast Matrix',
        'At-risk Project Gap'
      ],
      datasets: [
        {
          label: 'Manual Spreadsheet Workflow (Minutes)',
          data: [45, 30, 25, 60, 35],
          backgroundColor: '#EF4444',
          borderRadius: 8
        },
        {
          label: 'WorkforceIQ API Execution (Milliseconds)',
          data: [0.05, 0.08, 0.02, 0.04, 0.03],
          backgroundColor: '#22C55E',
          borderRadius: 8
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { labels: { color: textColor, font: { family: 'Plus Jakarta Sans', weight: '600' } } }
      },
      scales: {
        x: { grid: { color: gridColor }, ticks: { color: textColor } },
        y: { type: 'logarithmic', grid: { color: gridColor }, ticks: { color: textColor } }
      }
    }
  });
}
