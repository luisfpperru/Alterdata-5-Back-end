package br.com.alterdata.pack.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.alterdata.pack.exception.BadRequestException;
import br.com.alterdata.pack.exception.NotFoundException;
import br.com.alterdata.pack.model.Equipe;
import br.com.alterdata.pack.model.Usuario;
import br.com.alterdata.pack.repository.EquipeRepository;
import br.com.alterdata.pack.shared.EquipeDto;

@Service
public class EquipeServiceImpl implements EquipeService{
    
    @Autowired
    private EquipeRepository _repositorioEquipe;

    @Override
    public List<EquipeDto> obterTodos(Pageable pageable) {
        Page<Equipe> equipes = _repositorioEquipe.findAll(pageable);
        
        return equipes.stream().map(equipe -> new ModelMapper().map(equipe, EquipeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EquipeDto> obterPorId(Long id) {
        Optional<Equipe> encontrado = _repositorioEquipe.findByIdEquipe(id);

        if(!encontrado.isPresent()) {
            throw new NotFoundException("Não foi encontrado equipe com o ID: " + id);
        }
        return Optional.of(new ModelMapper().map(encontrado.get(),EquipeDto.class));
    }

    @Override
    public List<Equipe> obterPorNome(String nome) {
        List<Equipe> encontrado = _repositorioEquipe.findByNomeContainingIgnoreCase(nome);

        if (encontrado.size() == 0) {
            throw new NotFoundException("Não foi encontrado equipe com o nome: " + nome);
        }
        return encontrado;
    }

    @Override
    public List<Usuario> obterUsuariosPorLogin(Long idEquipe,String login) {
        
        Optional<Equipe> encontrado = _repositorioEquipe.findByIdEquipe(idEquipe);

        if (!encontrado.isPresent()) {
            throw new NotFoundException("Não foi encontrado nenhuma equipe com o Id: " + idEquipe);
        }

        List<Usuario> usuarios = encontrado.get().getMembros().stream()
                                                              .filter( usuario -> usuario.getLogin().contains(login))
                                                              .collect(Collectors.toList());

        if (usuarios.size() == 0) {
            throw new NotFoundException("Não foi encontrado nenhum usuario com o nome: " + login);
        }
        return usuarios;
    }

    @Override
    public Equipe criarEquipe(Equipe equipe) {
        equipe.setIdEquipe(null);

        verificarSeEquipeExiste(equipe);

        if (equipe.getNome() == "" || equipe.getNome() == null) {
            throw new BadRequestException("Nome não pode ser nulo!");
        }
        if (equipe.getCor1() == null){
            equipe.setCor1("#000");
        }
        if (equipe.getCor2() == null){
            equipe.setCor2("#fff");
        }
        Equipe novoEquipe = _repositorioEquipe.save(equipe);
        return novoEquipe;
    }
    
    @Override
    public Equipe atualizar(Long id, Equipe equipe) {
        Optional<Equipe> encontrado = _repositorioEquipe.findByIdEquipe(id);

        if(!encontrado.isPresent()) {
            throw new NotFoundException("Não foi encontrado equipe com o ID: " + id);
        }
        equipe.setIdEquipe(id);

        if(equipe.getNome() == "" || equipe.getNome() == null){
            throw new BadRequestException("Nome não pode ser nulo!");
        }
        Equipe equipeAtualizado = _repositorioEquipe.save(equipe);
        return equipeAtualizado;
    }

    @Override
    public void deletar(Long id) {
        Optional<Equipe> encontrado = _repositorioEquipe.findByIdEquipe(id);

        if (!encontrado.isPresent()) {
            throw new NotFoundException("Não existe equipe com o id informado: " + id);
        }

        for (Usuario membro : encontrado.get().getMembros()) {
            membro.setEquipe(null);
        }
        encontrado.get().setMembros(null);
        
        this._repositorioEquipe.deleteById(id);
    }

    private void verificarSeEquipeExiste(Equipe equipe) {
        Optional<Equipe> equipeExiste = _repositorioEquipe.findByNome(equipe.getNome());

        if (equipeExiste.isPresent()) {
            throw new BadRequestException("Opa! Já existe equipe com esse nome.");
        }
    }
}
