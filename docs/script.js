
   // SCRIPT PRINCIPAL


document.addEventListener('DOMContentLoaded', () => {
  // 1. Animação de Revelação ao Rolar (Scroll Reveal)
  const elementosRevelar = document.querySelectorAll('.revelar');

  const observarRevelacao = new IntersectionObserver((entries, observer) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('ativo');
        observer.unobserve(entry.target);
      }
    });
  }, {
    threshold: 0.15
  });

  elementosRevelar.forEach(el => observarRevelacao.observe(el));
  // Menu mobile (abre e fecha ao clicar no botão hambúrguer)
  const botaoMenu = document.querySelector('.menu-botao');
  const menuPrincipal = document.getElementById('menu-principal');

  if (botaoMenu && menuPrincipal) {
    botaoMenu.addEventListener('click', () => {
      const aberto = menuPrincipal.classList.toggle('aberto');
      botaoMenu.setAttribute('aria-expanded', String(aberto));
    });

    menuPrincipal.querySelectorAll('.menu__link').forEach((link) => {
      link.addEventListener('click', () => {
        menuPrincipal.classList.remove('aberto');
        botaoMenu.setAttribute('aria-expanded', 'false');
      });
    });
  }

  // 2. Animação de Contadores Numéricos
  const contadores = document.querySelectorAll('.contador');

  const animarContador = (el) => {
    const alvo = parseFloat(el.getAttribute('data-alvo')) || 0;
    const formato = el.getAttribute('data-formato') || 'inteiro';
    const duracao = 2000; // 2 segundos
    const inicio = performance.now();

    const atualizar = (tempoAtual) => {
      const decorrido = tempoAtual - inicio;
      const progresso = Math.min(decorrido / duracao, 1);

      // Efeito de desaceleração suave
      const valorAtual = alvo * (1 - (1 - progresso) * (1 - progresso));

      /* Adiciona "R$ " antes do valor durante e no fim da animação */
      if (formato === 'moeda') {
        el.textContent = 'R$ ' + valorAtual.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
      } else if (formato === 'percentual') {
        el.textContent = valorAtual.toFixed(2).replace('.', ',') + '%';
      } else {
        el.textContent = Math.floor(valorAtual).toLocaleString('pt-BR');
      }

      if (progresso < 1) {
        requestAnimationFrame(atualizar);
      } else {
        if (formato === 'moeda') el.textContent = 'R$ ' + alvo.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        else if (formato === 'percentual') el.textContent = alvo.toFixed(2).replace('.', ',') + '%';
        else el.textContent = alvo.toLocaleString('pt-BR');
      }
    };

    requestAnimationFrame(atualizar);
  };

  const observarContadores = new IntersectionObserver((entries, observer) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        animarContador(entry.target);
        observer.unobserve(entry.target);
      }
    });
  }, {
    threshold: 0.5
  });

  contadores.forEach(el => observarContadores.observe(el));

  // 3. Lógica do Botão de Instalação (PWA)
  let eventoInstalacao;
  const botaoInstalar = document.getElementById('botao-instalar');

  window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    eventoInstalacao = e;

    if (botaoInstalar) {
      botaoInstalar.removeAttribute('hidden');
    }
  });

  if (botaoInstalar) {
    botaoInstalar.addEventListener('click', async () => {
      if (!eventoInstalacao) return;

      eventoInstalacao.prompt();
      const { outcome } = await eventoInstalacao.userChoice;

      if (outcome === 'accepted') {
        console.log('Usuário instalou o aplicativo PWA');
      }

      eventoInstalacao = null;
      botaoInstalar.setAttribute('hidden', '');
    });
  }
});


// 4. REGISTRO DO SERVICE WORKER (PWA)

// Verifica se o navegador suporta Service Worker
if ('serviceWorker' in navigator) {
  // Espera a página terminar de carregar para registrar o arquivo
  window.addEventListener('load', () => {
    // Usa caminho relativo (sem barra no início) para funcionar no GitHub Pages
    navigator.serviceWorker.register('sw.js')
        .then(() => console.log('Service Worker registrado com sucesso!'))
        .catch((erro) => console.error('Erro ao registrar Service Worker:', erro));
  });
}