package br.com.eanassu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.eanassu.pojo.Funcionario;

@Repository
public class DaoFuncionario {
	private Connection connection;

	@Autowired
	public DaoFuncionario(DataSource dataSource) {
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public DaoFuncionario() {
	}

	public void insert(Funcionario funcionario) {
		String sql = "INSERT INTO FUNCIONARIOS VALUES(?,?,?,?)";
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, funcionario.getRe());
			pstmt.setString(2, funcionario.getNome());
			pstmt.setDate(3, new java.sql.Date(funcionario.getDataAdmissao().getTime()));
			pstmt.setDouble(4, funcionario.getSalario());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(Funcionario funcionario) {
		String sql = "DELETE FROM FUNCIONARIOS WHERE RE = ?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, funcionario.getRe());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Funcionario funcionario) {
		String sql = "UPDATE FUNCIONARIOS SET NOME=?,DATAADMISSAO=?,SALARIO=? WHERE RE=?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, funcionario.getNome());
			pstmt.setDate(2, new java.sql.Date(funcionario.getDataAdmissao().getTime()));
			pstmt.setDouble(3, funcionario.getSalario());
			pstmt.setInt(4, funcionario.getRe());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Funcionario> getLista() {
		String sql = "SELECT * FROM ALUNOS";
		List<Funcionario> lista = new ArrayList<Funcionario>();
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int re = rs.getInt(1);
				String nome = rs.getString(2);
				Date dataAdmissao = rs.getDate(3);
				Double salario = rs.getDouble(4);
				lista.add(new Funcionario(re, nome, dataAdmissao, salario));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

	public Funcionario buscarPeloRe(int re) {
		String sql = "SELECT * FROM FUNCIONARIOS WHERE RE=?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, re);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String nome = rs.getString(2);
				Date dataAdmissao = rs.getDate(3);
				Double salario = rs.getDouble(4);
				return new Funcionario(re, nome, dataAdmissao, salario);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
