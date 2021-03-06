package Export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Cell;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import BUS.PhieuNhapBUS;
import DAL.NhaCungCapDAL;
import DAL.NhanVienDAL;
import DAL.PhieuNhapDAL;
import DTO.NhaCungCapDTO;
import DTO.NhanVien;
import DTO.PhieuNhap;

public class WritePDF {
	Font fontData;
	Font fontData2;

	public WritePDF() {
		super();
		try {
			fontData = new Font(
					BaseFont.createFont("C:\\Windows\\Fonts\\Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 11,
					Font.NORMAL);
			fontData2 = new Font(
					BaseFont.createFont("C:\\Windows\\Fonts\\Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 18,
					Font.BOLD);
			fontData2.setColor(255, 33, 11);
		} catch (DocumentException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
	}

	public void xuatPDFPhieuNhap(JTable table, int ma) {

		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		String path = "";
		JFileChooser j = new JFileChooser();
		j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int x = j.showSaveDialog(j);
		if (x == JFileChooser.APPROVE_OPTION) {
			path = j.getSelectedFile().getPath();

		}

		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(path + ".pdf"));
			doc.open();

			Paragraph pdfTitle = new Paragraph(new Phrase("TH??NG TIN PHI???U NH???P", fontData2));
			// set title
			pdfTitle.setAlignment(Element.ALIGN_CENTER);
			// c??n gi???a
			doc.add(pdfTitle);
			doc.add(Chunk.NEWLINE);
			// xu???ng d??ng
			PhieuNhap pn = PhieuNhapDAL.getphieunhap(ma);
			System.out.println(pn);
			Chunk glue = new Chunk(new VerticalPositionMark());// Khoang trong giua hang

			Paragraph paraMaHoaDon = new Paragraph(
					new Phrase("Phi???u Nh???p: " + String.valueOf(pn.getMaPhieuNhap()), fontData));
			doc.add(paraMaHoaDon);
			doc.add(Chunk.NEWLINE);

			NhaCungCapDTO ncc = NhaCungCapDAL.getNhacungcap(pn.getMaNhaCung());
			// vi???t 1 h??m getnhacungcap ????? l???y t??n nh?? cung c???p
			NhanVien nv = NhanVienDAL.getnhanvien(pn.getMaNhanVien());
			// t????ng t??? v???i nh??n vi??n
			Paragraph para1 = new Paragraph();
			para1.setFont(fontData);
			para1.add(String.valueOf("Nh?? Cung C???p : " + ncc.getTenNCC()));
			para1.add(glue);
			doc.add(para1);
			doc.add(Chunk.NEWLINE);

			Paragraph para2 = new Paragraph();
			para2.setFont(fontData);

			para2.add(String.valueOf("T??n Nh??n Vi??n : " + nv.getTenNV()));
			para2.add(glue);
			doc.add(para2);
			doc.add(Chunk.NEWLINE);

			Paragraph para3 = new Paragraph();
			para3.setFont(fontData);
			para3.add(String.valueOf("Ng??y Nh???p : " + pn.getNgayNhap()));
			para3.add(glue);
			doc.add(para3);
			doc.add(Chunk.NEWLINE);

			// Xu???t table
			PdfPTable tbl = new PdfPTable(table.getColumnCount());
			for (int i = 0; i < dtm.getColumnCount(); i++) {
				String data = String.valueOf(dtm.getColumnName(i) + "");
				PdfPCell cell = new PdfPCell(new Phrase(data, fontData));

				tbl.addCell(cell);

			}
			float tong = 0;
			for (int i = 0; i < dtm.getRowCount(); i++) {
				for (int j1 = 0; j1 < dtm.getColumnCount(); j1++) {

					PdfPCell cell = new PdfPCell(new Phrase(dtm.getValueAt(i, j1).toString(), fontData));
					tbl.addCell(cell);

				}
				tong += Integer.parseInt(dtm.getValueAt(i, 5).toString());
			}
			// format l???i ti???n nh??n cho ?????p
			String pattern = "###,###.###";
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			String tongtong = decimalFormat.format(tong);
			doc.add(tbl);
			doc.add(Chunk.NEWLINE);
			Paragraph pdf2 = new Paragraph(new Phrase("T???ng Ti???n: " + tongtong + " VN??", fontData));
			// set title
			pdf2.setAlignment(Element.ALIGN_RIGHT);
			// c??n gi???a
			doc.add(pdf2);
			doc.add(Chunk.NEWLINE);
			// X??c nh???n
			Paragraph pdf = new Paragraph(new Phrase("X??c Nh???n Ho?? ????n", fontData));
			// set title
			pdf.setAlignment(Element.ALIGN_RIGHT);
			// c??n gi???a
			doc.add(pdf);
			doc.add(Chunk.NEWLINE);
			JOptionPane.showMessageDialog(null, "T???o pdf Th??nh C??ng");
			doc.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "T???o pdf Th???t B???i");
			e.printStackTrace();
		}

	}

	public void xuatPDF(JTable table) {

		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		String path = "";
		JFileChooser j = new JFileChooser();
		j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int x = j.showSaveDialog(j);
		if (x == JFileChooser.APPROVE_OPTION) {
			path = j.getSelectedFile().getPath();

		}

		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(path + ".pdf"));
			doc.open();
			PdfPTable tbl = new PdfPTable(table.getColumnCount());
			/*
			 * Paragraph paragraph = new Paragraph(); paragraph.add("V?? Ho??ng Ki???t");
			 * doc.add(paragraph);
			 */

			for (int i = 0; i < dtm.getColumnCount(); i++) {
				String data = String.valueOf(dtm.getColumnName(i) + "");
				PdfPCell cell = new PdfPCell(new Phrase(data, fontData));

				tbl.addCell(cell);

			}
			for (int i = 0; i < dtm.getRowCount(); i++) {
				for (int j1 = 0; j1 < dtm.getColumnCount(); j1++) {

					PdfPCell cell = new PdfPCell(new Phrase(dtm.getValueAt(i, j1).toString(), fontData));
					tbl.addCell(cell);

				}
			}

			doc.add(tbl);
			JOptionPane.showMessageDialog(null, "T???o pdf Th??nh C??ng");
			doc.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "T???o pdf Th???t B???i");
			e.printStackTrace();
		}

	}
}
