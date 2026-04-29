package MyCalc;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.SwingConstants;

public class MyCalc extends JFrame {
	
	private String exp = "";
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JButton btnNewButton_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyCalc frame = new MyCalc();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MyCalc() {
		setTitle("계산기");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 372, 405);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JLabel lblNewLabel = new JLabel("Status");
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		textField.setText("0");
		panel_1.add(textField, BorderLayout.NORTH);
		textField.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(5, 4, 10, 5));
		
		btnNewButton_1 = new JButton("7");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = exp + "7";
				textField.setText(exp);
			}
			
		});
		panel_2.add(btnNewButton_1);
		
		JButton btnNewButton_4 = new JButton("8");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = exp + "8";
				textField.setText(exp);
			}
		});
		panel_2.add(btnNewButton_4);
		
		JButton btnNewButton_2 = new JButton("9");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = exp + "9";
				textField.setText(exp);
			}
		});
		panel_2.add(btnNewButton_2);
		
		JButton btnNewButton_8 = new JButton("/");
		btnNewButton_8.addActionListener(e -> {
		    exp += "/";
		    textField.setText(exp);
		});

		panel_2.add(btnNewButton_8);
		
		JButton btnNewButton_12 = new JButton("4");
		btnNewButton_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = exp + "4";
				textField.setText(exp);
			}
		});
		panel_2.add(btnNewButton_12);
		
		JButton btnNewButton_3 = new JButton("5");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = exp + "5";
				textField.setText(exp);
			}
		});
		panel_2.add(btnNewButton_3);
		
		JButton btnNewButton_6 = new JButton("6");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = exp + "6";
				textField.setText(exp);
			}
		});
		panel_2.add(btnNewButton_6);
		
		JButton btnNewButton_9 = new JButton("*");
		
		btnNewButton_9.addActionListener(e -> {
		    exp += "*";
		    textField.setText(exp);
		});


		panel_2.add(btnNewButton_9);
		
		JButton btnNewButton = new JButton("1");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = exp + "1";
				textField.setText(exp);
			}
		});
		panel_2.add(btnNewButton);
		
		JButton btnNewButton_5 = new JButton("2");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = exp + "2";
				textField.setText(exp);
			}
		});
		panel_2.add(btnNewButton_5);
		
		JButton btnNewButton_7 = new JButton("3");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = exp + "3";
				textField.setText(exp);
			}
		});
		btnNewButton_7.setForeground(new Color(0, 0, 0));
		panel_2.add(btnNewButton_7);
		
		JButton btnNewButton_10 = new JButton("-");
		btnNewButton_10.addActionListener(e -> {
		    exp += "-";
		    textField.setText(exp);
		});
		panel_2.add(btnNewButton_10);
		
		JButton btnNewButton_11 = new JButton("0");
		btnNewButton_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = exp + "0";
				textField.setText(exp);
			}
		});
		panel_2.add(btnNewButton_11);
		
		JButton btnNewButton_13 = new JButton("C");
		btnNewButton_13.addActionListener(e -> {
		    exp = "";
		    textField.setText("0");
		});
		panel_2.add(btnNewButton_13);
		
		JButton btnNewButton_14 = new JButton("=");
		btnNewButton_14.addActionListener(e -> {
		    try {
		        String postfix = Infix2Postfix.convert(exp);
		        double result = Calc.eval(postfix);
		        textField.setText(String.valueOf(result));

		    } catch(Exception ex) {
		        textField.setText("수식을 입력하세요.");
		        exp = "";
		    }
		});
		panel_2.add(btnNewButton_14);
		
		JButton btnNewButton_15 = new JButton("+");
		btnNewButton_15.addActionListener(e -> {
		    exp += "+";
		    textField.setText(exp);
		});


		panel_2.add(btnNewButton_15);
		
		JButton btnNewButton_17 = new JButton("(");
		btnNewButton_17.addActionListener(e -> {
		    exp += "(";
		    textField.setText(exp);
		});

		panel_2.add(btnNewButton_17);
		
		JButton btnNewButton_18 = new JButton(")");
		btnNewButton_18.addActionListener(e -> {
		    exp += ")";
		    textField.setText(exp);
		});

		panel_2.add(btnNewButton_18);
		
		JButton btnNewButton_16 = new JButton(".");
		btnNewButton_16.addActionListener(e -> {
		    exp += ".";
		    textField.setText(exp);
		});
		panel_2.add(btnNewButton_16);

	}

	public JTextField getTextField() {
		return textField;
	}
	public JButton getBtnNewButton_1() {
		return btnNewButton_1;
	}
}

