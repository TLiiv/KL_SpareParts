package tliiv.dev.spareparts.views.spareparts;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.apache.el.lang.ELArithmetic.add;

@PageTitle("spare-parts")
@Route("")
@Menu(order = 0, icon = "line-awesome/svg/pencil-ruler-solid.svg")
public class SparePartsView extends VerticalLayout {
    Grid<String[]> grid = new Grid<>();

    public SparePartsView() {

        var importButton = new Button("Import",e->ReadFromClasspath());
        var buffer = new MemoryBuffer();
        var upload = new Upload(buffer);
        upload.addSucceededListener(e->{
            displayCsv(buffer.getInputStream());
        });
        add(grid, new HorizontalLayout(importButton,upload));

    }

    private void ReadFromClasspath(){
        displayCsv(getClass().getClassLoader().getResourceAsStream("csv/LE.txt"));
    }

    private void displayCsv(InputStream resourceAsStream) {
        var parser = new CSVParserBuilder().withSeparator('|').build();
        var reader = new CSVReaderBuilder(new InputStreamReader(resourceAsStream)).withCSVParser(parser).build();

        try {
            var entries = reader.readAll();
            var headers = entries.get(0);

            for(int i = 0; i < headers.length; i++){
                int colIndex = i;
                grid.addColumn(row->row[colIndex]).setHeader(headers[colIndex]);

                grid.setItems(entries.subList(1,entries.size()));
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }


    }
}
