import java.util.*;
import java.sql.Connection;

import model.Usuario;
import model.dao.ConexaoFactory;
import model.dao.IUsuarioDAO;
import model.dao.UsuarioDAO;
import model.reposirory.*;
import services.UsuarioService;

public class MvcApp {
    public static void main(String[] args) throws Exception {

        /*
         * Este conjunto de instruções inicializaram as dependencias 
         * para o uso do serviço de contatos utilizando o repositório
         * com o SGBD MySQL.
         */
        Connection conexao = ConexaoFactory.getConexao();
        IUsuarioDAO dao = new UsuarioDAO(conexao);
        UsuarioRepository reposirory = new UsuarioRepositoryMySQLImpl(dao);

        UsuarioMemoriaRepositoryImpl repo = new UsuarioMemoriaRepositoryImpl();
        UsuarioService service = new UsuarioService(reposirory);
        
        /*
         * Utilize as leituras em console se preferir.
         * Scanner in = new Scanner(System.in);
         * System.out.println("Digite o nome: ");
         * String nome = in.nextLine();
         * System.out.println("Digite o e-mail: ");
         * String email = in.nextLine();
         * System.out.println("Digite o senha: ");
         * String senha = in.nextLine();
         */
        

        Usuario u1 = new Usuario(null, "Bia", "beatriz@email.com", "12345");
        service.criar(u1);

        // Descomente o trecho abaixo para persisitir em baco de dados e consulte o banco de dados
        

        // Criar mais 2 usuarios.

        Usuario u2 = new Usuario(null, "Ana", "ana@email.com", "67891");
        Usuario u3 = new Usuario(null, "Julia", "ju@email.com", "23456");

        // Chamada do metodo de persistencia
        service.criar(u2);
        service.criar(u3);

        // Exibir os usuarios cadastrados

        System.out.println("Usuários cadastrados:");
        List<Usuario> usuarios = service.obterTodos();
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }

        // Remover o primeiro usuario criado.

        service.excluir(u1.getId());

        // Buscar e exibir o segundo usuario criado com base no e-mail

        Usuario usuarioBuscado = service.obterTodos().stream()
            .filter(usuario -> usuario.getEmail().equals(u2.getEmail()))
            .findFirst()
            .orElse(null);
        System.out.println("Usuário buscado pelo e-mail: " + usuarioBuscado);

        // Exibir os usuarios cadastrados

        System.out.println("Usuários cadastrados após remoção:");
        usuarios = service.obterTodos();
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }

        // Altere o repositório MySQL pelo repositório em memória    

        UsuarioRepository memoriaRepository = new UsuarioMemoriaRepositoryImpl();
        UsuarioService memoriaService = new UsuarioService(memoriaRepository);

        // Salvar os mesmos usuários no repositório em memória
        memoriaService.criar(u2);
        memoriaService.criar(u3);

        // Exibir os usuários cadastrados no repositório em memória
        System.out.println("Usuários cadastrados no repositório em memória:");
        List<Usuario> memoriaUsuarios = memoriaService.obterTodos();
        for (Usuario usuario : memoriaUsuarios) {
            System.out.println(usuario);
        }
    }
}
