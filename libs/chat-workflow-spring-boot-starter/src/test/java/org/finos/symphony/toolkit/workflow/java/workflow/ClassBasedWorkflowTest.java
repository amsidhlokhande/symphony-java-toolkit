package org.finos.symphony.toolkit.workflow.java.workflow;

import org.finos.symphony.toolkit.workflow.java.Exposed;
import org.finos.symphony.toolkit.workflow.java.Work;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassBasedWorkflowTest {

    ClassBasedWorkflow classBasedWorkflow;

    @BeforeEach
    public void beforeEach() {
        this.classBasedWorkflow = new ClassBasedWorkflow("");
    }

    @Test
    public void testRegisterWorkClasses() {
        Set<Class<?>> workClasses = new HashSet<>();
        workClasses.add(Work1.class);
        this.classBasedWorkflow.registerWorkClasses(workClasses);
        List<Class<?>> dataTypes = this.classBasedWorkflow.getDataTypes();
        Assertions.assertNotNull(dataTypes);
        Assertions.assertFalse(dataTypes.isEmpty());
        Assertions.assertEquals(1, dataTypes.size());
    }

    @Test
    public void testRegisterWorkClassesWithEmptySetOfClass() {
        Set<Class<?>> workClasses = new HashSet<>();
        this.classBasedWorkflow.registerWorkClasses(workClasses);
        List<Class<?>> dataTypes = this.classBasedWorkflow.getDataTypes();
        Assertions.assertNotNull(dataTypes);
        Assertions.assertTrue(dataTypes.isEmpty());
    }


    @Test()
    public void testRegisterWorkClassesWithDuplicateClasses() {
        Set<Class<?>> workClasses = new HashSet<>();
        workClasses.add(Work1.class);
        workClasses.add(Work2.class);
        Exception exception = Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            this.classBasedWorkflow.registerWorkClasses(workClasses);
        });
        String expectedMessage = "Methods clash: public java.lang.String org.finos.symphony.toolkit.workflow.java.workflow.Work2.getDescription() with public java.lang.String org.finos.symphony.toolkit.workflow.java.workflow.Work1.getDescription()";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testScanPackagesWithTypeFilter(){
        Set<String> packages = new HashSet<>();
        packages.add(Work1.class.getPackage().getName());
        Set<Class<?>> classes = this.classBasedWorkflow.scanPackagesWithTypeFilter(packages, new AnnotationTypeFilter(Work.class));
        Assertions.assertTrue(classes.contains(Work1.class));

    }

    @Test
    public void testScanPackagesWithTypeFilterWork(){
        Set<String> packages = new HashSet<>();
        packages.add(Work1.class.getPackage().getName());
        Set<Class<?>> classes = this.classBasedWorkflow.scanPackagesWithTypeFilter(packages, new AnnotationTypeFilter(Work.class));
        Assertions.assertFalse(classes.isEmpty());
        Assertions.assertTrue(classes.contains(Work1.class));

    }
    @Test
    public void testScanPackagesWithTypeFilterEmpty(){
        Set<String> packages = new HashSet<>();
        Set<Class<?>> classes = this.classBasedWorkflow.scanPackagesWithTypeFilter(packages, new AnnotationTypeFilter(Work.class));
        Assertions.assertTrue(classes.isEmpty());

    }
}

@Work
class Work1{

    String description;

    @Exposed
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

@Work
class Work2{

    String description;

    @Exposed
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}