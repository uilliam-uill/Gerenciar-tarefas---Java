package trfs;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class BuscarTurmas extends JFrame {
	private JPanel contentPane;
	private JTable tabela1Ef;
	private JComboBox procurarTurmas;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JLabel lblNewLabel;
	private JTextField nomeTarefa;
	private JLabel lblNewLabel_1;
	private JTextField descricaoTexto;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JTextField dataConclusaotxt;
	private JButton btnNewButton_2;
	private JButton btnNewButton_3;

	/**
	 * Launch the application.
	 */
	   
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BuscarTurmas frame = new BuscarTurmas();  //criando JFrame
				
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public BuscarTurmas() throws ClassNotFoundException, SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 948, 488);
		contentPane = new JPanel();   //criando JPanel
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//criação de colunas da tabela
		String [] colunas = {"ID","TITULO TAREFA", "DESCRIÇÃO TAREFA", "DATA DE INSCRIÇÃO", "DATA DE CONCLUSÃO"};
		DefaultTableModel primerioEf = new DefaultTableModel(colunas,0);
		
		//botão para acessar tarefas já registradas
		btnNewButton = new JButton("ACESSAR DADOS");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			        try {
			            Class.forName("com.mysql.cj.jdbc.Driver");  //conectando ao banco
			            
			            String url = "jdbc:mysql://localhost:3306/colegio";
			            String usuario = "root";
			            String senha = "1234";
			            
			            Connection conexao = DriverManager.getConnection(url, usuario, senha);
			            Statement con = conexao.createStatement();
			            ResultSet resultado = con.executeQuery("SELECT * FROM sys.tarefas");
			            
			            DefaultTableModel tabela = (DefaultTableModel) tabela1Ef.getModel();
						tabela.setRowCount(0);
			            
			            while (resultado.next()) {
			            	int id = resultado.getInt("id");
			                String tTarefa = resultado.getString("tituloTarefa");
			                String dTarefa = resultado.getString("descricaoTarefa");
			                String dCriacao = resultado.getString("dataCriacao");
			                String dConclusao = resultado.getString("dataConclusao");
			                Object[] linha = {id,tTarefa, dTarefa, dCriacao, dConclusao}; //adicionando dados do bacno na tabela
			                tabela.addRow(linha);
			            }
			            resultado.close();
			            con.close();
			            conexao.close();
			        } catch (ClassNotFoundException | SQLException e1) {
			            e1.printStackTrace();
			        }
			    
			}	});
		
		btnNewButton_1 = new JButton("REGISTRAR TAREFA");  //registrando uma nova tarefa
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

				String nTarefa = nomeTarefa.getText();
				String DTarefa = descricaoTexto.getText();
				String dCadastro = lblNewLabel_3.getText();
				String dConclusao = dataConclusaotxt.getText();
				
				DefaultTableModel tabela = (DefaultTableModel) tabela1Ef.getModel();
			   	ResultSet rs = null;
							
			 try {
				 
				 if (nomeTarefa.getText().trim().isEmpty() || descricaoTexto.getText().trim().isEmpty())  //impedindo que seja nulo os campos JTextField
						 {
						JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
					} else {
						
						Class.forName("com.mysql.cj.jdbc.Driver");
						
		                 String url = "jdbc:mysql://localhost:3306/sys";
		                 String usuario = "root";
		                 String senha = "1234";
		                 
		                 Connection conexao = DriverManager.getConnection(url, usuario, senha);

		                 PreparedStatement con = conexao.prepareStatement("INSERT INTO sys.tarefas(tituloTarefa, descricaoTarefa, dataCriacao, dataConclusao)	"
		                 		+ " VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
		                 	con.setString(1, nTarefa);
		                 	con.setString(2, DTarefa);
		                 	con.setString(3, dCadastro);
		                 	con.setString(4, dConclusao);
		                 	con.executeUpdate();
		              
		                 	rs = con.getGeneratedKeys();

							if (rs.next()) {
								int id = rs.getInt(1);
								String[] data = { String.valueOf(id), nTarefa, DTarefa, dConclusao };
								tabela.addRow(data);  //adiconando dado do bacno na tabela
							}
		                 nomeTarefa.requestFocus();
		                 nomeTarefa.setText("");
		                 descricaoTexto.setText("");
		                 dataConclusaotxt.setText("");
		                 
		              if(conexao!=null) {
		                	 
		                	 JOptionPane.showMessageDialog(null, "REGISTRADO COM SUCESSO");

		                 }
						
					}
		
				 } catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		tabela1Ef = new JTable(primerioEf);
		JScrollPane scroltabela1Ef = new JScrollPane(tabela1Ef);
		scroltabela1Ef.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int row = tabela1Ef.getSelectedRow();

				nomeTarefa.setText(tabela1Ef.getValueAt(row, 1).toString());
				descricaoTexto.setText(tabela1Ef.getValueAt(row, 2).toString());

				nomeTarefa.requestFocus();
				
			}
		});
		
		lblNewLabel = new JLabel("TITULO TAREFA");
		
		nomeTarefa = new JTextField();
		nomeTarefa.setColumns(10);
		
		lblNewLabel_1 = new JLabel("DESCRIÇÃO TAREFA");
		
		descricaoTexto = new JTextField();
		descricaoTexto.setColumns(10);
		
		lblNewLabel_2 = new JLabel("DATA INCLUSÃO");
		
		lblNewLabel_3 = new JLabel("New label");
		 LocalDate dataAtual = LocalDate.now();
		 	DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		 		String dataFormatada = dataAtual.format(formatoData);
	        lblNewLabel_3.setText(dataFormatada);
		
		lblNewLabel_4 = new JLabel("DATA PARA CONCLUSÃO");
		
		dataConclusaotxt = new JTextField();
		dataConclusaotxt.setColumns(10);
		
		//botão de apagar
		
		btnNewButton_2 = new JButton("APAGAR");   //botão para apgar uma tarefa
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			
				
				
				int row = tabela1Ef.getSelectedRow();
				 try {
				
					if(row == -1) {
						JOptionPane.showMessageDialog(null, "Selecione a linha que quer excluir");
					}else {
						int id = Integer.parseInt(tabela1Ef.getValueAt(row, 0).toString());
						if(JOptionPane.showConfirmDialog(null, "Tem certezar que quer remover?", "remover",
								JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
							
							Class.forName("com.mysql.cj.jdbc.Driver");
							
			                String url = "jdbc:mysql://localhost:3306/sys";
			                String usuario = "root";
			                String senha = "1234";
			                
			                Connection conexao = DriverManager.getConnection(url, usuario, senha);

			                PreparedStatement con = conexao.prepareStatement("DELETE FROM SYS.TAREFAS WHERE id = ?");
			                con.setInt(1, id);
			                con.executeUpdate();
			                con.close();
							((DefaultTableModel)tabela1Ef.getModel()).removeRow(row);
						} 
									
					}

                }
				  catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
			
		    
		    
		});
	
			
		//botão de atualizar
		btnNewButton_3 = new JButton("ATUALIZAR");   //botão para atualizar dados de uma tarefa
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				DefaultTableModel tabela = (DefaultTableModel) tabela1Ef.getModel();
				
				 try {
					int row = tabela1Ef.getSelectedRow();
					int id = Integer.parseInt(tabela.getValueAt(row, 0).toString());
						
					 String name = nomeTarefa.getText();
					 String descri = descricaoTexto.getText();
					 String dtins = lblNewLabel_3.getText();
					 String dtcon = dataConclusaotxt.getText();
				Class.forName("com.mysql.cj.jdbc.Driver");
				
                String url = "jdbc:mysql://localhost:3306/sys";
                String usuario = "root";
                String senha = "1234";
                
                Connection conexao = DriverManager.getConnection(url, usuario, senha);

                PreparedStatement con = conexao.prepareStatement("UPDATE sys.tarefas SET tituloTarefa = ? , descricaoTarefa = ? , "
                		+ " dataCriacao = ? , dataConclusao = ? where id = ?");
                con.setString(1, name);
                con.setString(2, descri);
                con.setString(3, dtins);
                con.setString(4, dtcon);
                con.setInt(5, id);
                con.executeUpdate();
                
                tabela.setValueAt(nomeTarefa.getText(), row, 1);
				tabela.setValueAt(descricaoTexto.getText(), row, 2);
				tabela.setValueAt(dataConclusaotxt.getText(), row, 4);
				
				JOptionPane.showMessageDialog(null, "Atualizado com sucesso !");
				
                con.close();
				
                nomeTarefa.setText("");
                descricaoTexto.setText("");
                dataConclusaotxt.setText("");
				}
				 catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					}
				 }
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_1)
						.addComponent(descricaoTexto)
						.addComponent(nomeTarefa, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
						.addComponent(lblNewLabel_2)
						.addComponent(lblNewLabel_3)
						.addComponent(dataConclusaotxt)
						.addComponent(lblNewLabel_4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewButton_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewButton_2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewButton_3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(135)
					.addComponent(scroltabela1Ef, GroupLayout.PREFERRED_SIZE, 545, GroupLayout.PREFERRED_SIZE)
					.addGap(87))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(14)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(nomeTarefa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(descricaoTexto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_3)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_4)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(dataConclusaotxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnNewButton_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_3)
					.addGap(18)
					.addComponent(btnNewButton)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(scroltabela1Ef, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);

		}
}	