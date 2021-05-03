package org.finos.symphony.toolkit.workflow.sources.symphony.handlers.freemarker;

import org.finos.symphony.toolkit.json.EntityJson;
import org.finos.symphony.toolkit.workflow.sources.symphony.elements.edit.TableAddRow;
import org.finos.symphony.toolkit.workflow.sources.symphony.elements.edit.TableDeleteRows;
import org.finos.symphony.toolkit.workflow.sources.symphony.elements.edit.TableEditRow;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class MapConverter extends AbstractTableConverter {

    public MapConverter() {
        super(LOW_PRIORITY);
    }

    @Override
    protected Object rowDetails(Type t, boolean editMode, Variable variable, EntityJson ej, WithField rowDetail, WithType controller) {
        StringBuilder sb = new StringBuilder();
        Variable subVar = variable.index();
        // handle each field
        sb.append(beginMapIterator(variable, subVar));
        sb.append(indent(subVar.depth) + "<tr>");

        Type[] actualTypeArguments = ((ParameterizedType) t).getActualTypeArguments();

        for (int i = 0; i < actualTypeArguments.length; i++) {
            Class<?> elementClass = (Class<?>) actualTypeArguments[i];

            TypeConverter elementTypeConverter = controller.getConverter(elementClass, controller);
            if (elementTypeConverter instanceof SimpleTypeConverter) {
                sb.append("<td>");
                if (i == 0) {
                    sb.append(((SimpleTypeConverter) elementTypeConverter).apply(controller, elementClass, false, subVar, ej, rowDetail));
                } else if (i == 1) {
                    sb.append("${" + variable.getDataPath() + "[" + subVar.getDataPath() + "]!''}");
                }
                sb.append("</td>");
            } else if (elementTypeConverter instanceof ComplexTypeConverter) {
                String value = ((ComplexTypeConverter) elementTypeConverter).apply(controller, elementClass, false, subVar, ej, rowDetail);
                sb.append(value.replace(subVar.getDataPath(), variable.getDataPath() + "[" + subVar.getDataPath() + "]"));

            } else {
                throw new UnsupportedOperationException();
            }

            if (editMode) {
                sb.append(indent(subVar.depth + 1) + "<td " + CENTER_AND_WIDTH_ALIGN + "><checkbox name=\"" + variable.getFormFieldName() + ".${" + subVar.getDataPath() + "?index}." + TableDeleteRows.SELECT_SUFFIX + "\" /></td>");
                sb.append(indent(subVar.depth + 1) + "<td " + CENTER_ALIGN + "><button name=\"" + variable.getFormFieldName() + "[${" + subVar.getDataPath() + "?index}]." + TableEditRow.EDIT_SUFFIX + "\">Edit</button></td>");
            }
        }
        sb.append(indent(subVar.depth) + "</tr>");
        sb.append(endIterator(variable));
        return sb.toString();
    }

    @Override
    protected Object rowHeaders(Type t, boolean editMode, Variable variable, EntityJson ej, WithField headerDetails, WithType controller) {

        StringBuilder sb = new StringBuilder();
        Type[] actualTypeArguments = ((ParameterizedType) t).getActualTypeArguments();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            Class<?> elementClass = (Class<?>) actualTypeArguments[i];
            TypeConverter elementTypeConverter = controller.getConverter(elementClass, controller);

            if (elementTypeConverter instanceof SimpleTypeConverter) {
                if (i == 0) sb.append("<td><b>Key</b></td>");
                if (i == 1) sb.append("<td><b>Value</b></td>");
            } else if (elementTypeConverter instanceof ComplexTypeConverter) {
                if (i == 0) throw new UnsupportedOperationException();
                sb.append(((ComplexTypeConverter) elementTypeConverter).withFields(controller, elementClass, editMode, variable, ej, headerDetails));
            } else {
                throw new UnsupportedOperationException();
            }
        }

        if (editMode) {
            sb.append(indent(variable.depth + 1) + "<td " + CENTER_ALIGN + "><button name=\"" + variable.getFormFieldName() + "." + TableDeleteRows.ACTION_SUFFIX
                    + "\">Delete</button></td>");
            sb.append(indent(variable.depth + 1) + "<td " + CENTER_ALIGN + "><button name=\"" + variable.getFormFieldName() + "." + TableAddRow.ACTION_SUFFIX
                    + "\">New</button></td>");
        }

        return sb.toString();
    }

    @Override
    public boolean canConvert(Type t) {
        if (t instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) t).getRawType();
            return (rawType instanceof Class<?>) && Map.class.isAssignableFrom((Class<?>) rawType);
        } else {
            return false;
        }
    }

    @Override
    public String apply(WithType controller, Type t, boolean editMode, Variable variable, EntityJson ej, WithField showDetails) {
        if (null == showDetails) return "...";

        if (showDetails.expand()) {
            return createTable(t, editMode, variable, ej, tableColumnNames(), tableColumnValues(), controller);
        } else {
            return text(variable, "!''");
        }
    }

    private String beginMapIterator(Variable variable, Variable reg) {
        return indent(variable.depth) + "<#list " + variable.getDataPath() + "?keys as " + reg.getDataPath() + ">";
    }
}
