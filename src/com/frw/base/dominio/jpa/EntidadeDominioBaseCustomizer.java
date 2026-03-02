/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.dominio.jpa;

import java.util.Vector;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.OneToManyMapping;

import com.frw.base.dominio.base.EntidadeDominioBase;

/**
 *
 * @author Marcelo Alves
 */
public class EntidadeDominioBaseCustomizer implements DescriptorCustomizer {

    public void customize(ClassDescriptor desc) {

        Vector<DatabaseMapping> mappings = desc.getMappings();

        for (DatabaseMapping databaseMapping : mappings) {
            if(databaseMapping instanceof OneToManyMapping) {
                OneToManyMapping oneToManyMapping = (OneToManyMapping) databaseMapping;

                if(EntidadeDominioBase.class.isAssignableFrom(oneToManyMapping.getReferenceClass())) {
                    Expression origExp = oneToManyMapping.buildSelectionCriteria();
                    ExpressionBuilder b = origExp.getBuilder();
                    Expression constantExp = b.get("excluido").notEqual(true);
                    Expression newExp = origExp.and(constantExp);
                    oneToManyMapping.setSelectionCriteria(newExp);
                }
            }
        }
    }
}