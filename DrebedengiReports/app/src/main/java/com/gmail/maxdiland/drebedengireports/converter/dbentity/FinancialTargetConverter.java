package com.gmail.maxdiland.drebedengireports.converter.dbentity;

import com.gmail.maxdiland.drebedengireports.bo.FinancialTargetBO;
import com.gmail.maxdiland.drebedengireports.db.entity.FinancialTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * author Max Diland
 */
public class FinancialTargetConverter implements EntityConverter<FinancialTarget, FinancialTargetBO> {
    private static final int ID_NO_PARENT = -1;

    @Override
    public FinancialTargetBO convert(FinancialTarget objectToConvert) {
        FinancialTargetBO financialTarget = new FinancialTargetBO();
        financialTarget.setId( objectToConvert.getId() );
        financialTarget.setRoot( hasTargetNoParent(objectToConvert) );
        financialTarget.setName( objectToConvert.getName() );
        return financialTarget;
    }

    @Override
    public FinancialTargetBO[] convert(FinancialTarget[] objectsToConvert) {
        // No need to refactor this method due to further migration to some ORM
        List<FinancialTarget> financialTargets = new ArrayList<>( Arrays.asList(objectsToConvert) );
        Map<Integer, FinancialTargetBO> parentTargets = new HashMap<>();
//        List<FinancialTargetBO> parentTargets = new ArrayList<>();

        // Choose root financial targets
        for (Iterator<FinancialTarget> iter = financialTargets.iterator(); iter.hasNext();) {
            FinancialTarget financialTarget = iter.next();
            if ( hasTargetNoParent(financialTarget) ) {
                parentTargets.put(financialTarget.getServerId(), convert(financialTarget));
                iter.remove();
            }
        }

        // Add child financial targets to root financial targets
        for (FinancialTarget childFinancialTarget : financialTargets) {
            FinancialTargetBO parent = parentTargets.get(childFinancialTarget.getParentId());
            if (parent != null) {
                if (parent.getChilds() == null) {
                    parent.setChilds( new ArrayList<FinancialTargetBO>() );
                }
                parent.getChilds().add( convert(childFinancialTarget) );
            }
        }

        List<FinancialTargetBO> parentFinancialTargetBOs =
                new ArrayList<>(parentTargets.values());

        Collections.sort(parentFinancialTargetBOs, new FinancialTargetComparator());

        List<FinancialTargetBO> sortedRootsAndChilds = new ArrayList<>();
        for (FinancialTargetBO financialTarget : parentFinancialTargetBOs) {
            sortedRootsAndChilds.add(financialTarget);
            if (financialTarget.getChilds() != null) {
                sortedRootsAndChilds.addAll(financialTarget.getChilds());
            }
        }

        return sortedRootsAndChilds.toArray( new FinancialTargetBO[sortedRootsAndChilds.size()] );
    }

    private boolean hasTargetNoParent(FinancialTarget target) {
        return ID_NO_PARENT == target.getParentId();
    }

    private class FinancialTargetComparator implements Comparator<FinancialTargetBO> {

        @Override
        public int compare(FinancialTargetBO left, FinancialTargetBO right) {
            return left.getId() - right.getId();
        }
    }
}
