import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ExamResultSwing extends JFrame {
    JTextField roll = new JTextField(5), subject = new JTextField(10), marks = new JTextField(5);
    DefaultTableModel model = new DefaultTableModel(new String[]{"Roll", "Subject", "Marks"}, 0);

    String url = "jdbc:mysql://localhost:3306/examdb?useSSL=false&serverTimezone=UTC";
    String user = "root", pass = "Anet#484";

    public ExamResultSwing() {
        setTitle("Exam Results");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.add(new JLabel("Roll:")); top.add(roll);
        top.add(new JLabel("Subject:")); top.add(subject);
        top.add(new JLabel("Marks:")); top.add(marks);

        JButton addBtn = new JButton("Add");
        JButton delBtn = new JButton("Delete");
        top.add(addBtn); top.add(delBtn);
        add(top, BorderLayout.NORTH);

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addBtn.addActionListener(e -> addRecord());
        delBtn.addActionListener(e -> deleteRecord());
        loadTable();
    }

    void loadTable() {
        model.setRowCount(0);
        try (Connection c = DriverManager.getConnection(url, user, pass);
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery("SELECT * FROM Results")) {
            while (r.next())
                model.addRow(new Object[]{r.getInt(1), r.getString(2), r.getInt(3)});
        } catch (Exception e) { e.printStackTrace(); }
    }

    void addRecord() {
        try (Connection c = DriverManager.getConnection(url, user, pass);
             PreparedStatement p = c.prepareStatement("INSERT INTO Results VALUES(?,?,?)")) {
            p.setInt(1, Integer.parseInt(roll.getText()));
            p.setString(2, subject.getText());
            p.setInt(3, Integer.parseInt(marks.getText()));
            p.executeUpdate();
            loadTable();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    void deleteRecord() {
        try (Connection c = DriverManager.getConnection(url, user, pass);
             PreparedStatement p = c.prepareStatement("DELETE FROM Results WHERE roll_no=?")) {
            p.setInt(1, Integer.parseInt(roll.getText()));
            p.executeUpdate();
            loadTable();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExamResultSwing().setVisible(true));
    }
}
