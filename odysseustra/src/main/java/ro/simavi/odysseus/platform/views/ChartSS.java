package ro.simavi.odysseus.platform.views;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.simavi.odysseus.platform.controllers.WorkflowInstanceController;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("chartSS")
@ViewScoped
@Getter
@Setter
public class ChartSS implements Serializable {

    @Autowired
    private WorkflowInstanceController workflowInstanceController;

    private PieChartModel pieModel1;
    private PieChartModel pieModel2;

    @PostConstruct
    public void init() {

        createPieModel1();
        createPieModel2();
    }


    private void createPieModel1() {
        pieModel1= new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();

        Map<String, Long> counts = workflowInstanceController.countWorkflowInstancesByStatus();
        values.add(counts.getOrDefault("Active", 0L));
        values.add(counts.getOrDefault("Inactive", 0L));


        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");

        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("workflow_instance active");
        labels.add("workflow_instance inactive");

        data.setLabels(labels);

        pieModel1.setData(data);
    }
    private void createPieModel2() {
        pieModel2= new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();

        Map<String, Long> counts = workflowInstanceController.countWorkflowInstanceNodesByStatus();
        values.add(counts.getOrDefault("Active", 0L));
        values.add(counts.getOrDefault("Inactive", 0L));


        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");

        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("workflow_instance_node active");
        labels.add("workflow_instance_node inactive");

        data.setLabels(labels);

        pieModel2.setData(data);
    }




}