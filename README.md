# STOKI

Sistema desktop de controle de depósito para um mercado pequeno, feito em Java Swing e PostgreSQL.

## Como rodar

1. Instale o PostgreSQL e o JDK 25.
2. Abra esta pasta no IntelliJ.
3. Copie `banco.properties.exemplo` com o nome `banco.properties` e coloque a senha do seu PostgreSQL.
4. Rode `Principal`. Na primeira vez, o sistema cria o banco `stoki`, as tabelas e os dados de demonstração.
5. Rode `Teste`. A seção BASE deve mostrar "Passou" em todos os itens.

| Usuário | Senha | Perfil |
| --- | --- | --- |
| `gerente` | `123` | Gerente/Dono |
| `estoque` | `123` | Responsável pelo estoque |
| `repositor` | `123` | Repositor |

Para voltar o banco local ao estado inicial, rode `RecriarBanco`.

## Quem cuida de cada arquivo

| Dev | Arquivos |
| --- | --- |
| Gabriel | Base do projeto, `TelaEntrada`, `TelaSaida`, `TelaPerda`, `TelaContagem`, `TelaMapa`, `EstoqueController`, `ContagemController`, `EstoqueService`, `ContagemService`, `LoteDAO`, `MovimentacaoDAO`, `ContagemDAO`, `EstoqueTest` |
| Erick | `TelaProduto`, `TelaEstoque`, `TelaPainel`, `TelaRelatorios`, `ProdutoController`, `RelatorioController`, `ProdutoService`, `RelatorioService`, `ProdutoDAO`, `RelatorioDAO`, `ProdutoTest` |
| Renan | `TelaFornecedor`, `TelaUsuario`, `FornecedorController`, `UsuarioController`, `FornecedorService`, `UsuarioService`, `FornecedorDAO`, `UsuarioDAO`, `FornecedorTest`, `UsuarioTest` |
| Matheus | Pasta `docs` |

Altere somente os seus arquivos. Se precisar mudar algo na base, fale com o Gabriel antes.

## Regras do código

- O caminho é sempre Tela, Controller, Service, DAO e banco.
- `TelaSetor` é o exemplo pronto desse caminho.
- Para listas de setores, fornecedores e produtos, use `CatalogoService`.
- Use `BigDecimal` para quantidades e valores, e `LocalDate` para datas.
- Use `Utilidade` para converter texto, data e dinheiro.
- Mostre erros como mensagens claras, usando `StokiException`.
- Comente todas as classes novas em português.
- Nunca envie `banco.properties` ao GitHub.
