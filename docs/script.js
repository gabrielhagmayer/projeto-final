
   SCRIPT PRINCIPAL (script.js)


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

 
  const contadores = document.querySelectorAll('.contador');

  const animarContador = (el) => {
    const alvo = parseFloat(el.getAttribute('data-alvo')) || 0;
    const formato = el.getAttribute('data-formato') || 'inteiro';
    const duracao = 2000; // 2 segundos
    const inicio = performance.now();

    const atualizar = (tempoAtual) => {
      const decorrido = tempoAtual - inicio;
      const progresso = Math.min(decorrido / duracao, 1);
      
     Efeito de desaceleração suave
      const valorAtual = alvo * (1 - (1 - progresso) * (1 - progresso));

      if (formato === 'moeda') {
        el.textContent = valorAtual.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
      } else if (formato === 'percentual') {
        el.textContent = valorAtual.toFixed(2).replace('.', ',') + '%';
      } else {
        el.textContent = Math.floor(valorAtual).toLocaleString('pt-BR');
      }

      if (progresso < 1) {
        requestAnimationFrame(atualizar);
      } else {
        if (formato === 'moeda') el.textContent = alvo.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
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