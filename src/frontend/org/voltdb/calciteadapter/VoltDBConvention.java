/* This file is part of VoltDB.
 * Copyright (C) 2008-2017 VoltDB Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with VoltDB.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.voltdb.calciteadapter;

import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.plan.RelTraitSet;
import org.voltdb.calciteadapter.rel.VoltDBRel;

public enum VoltDBConvention implements Convention {
    INSTANCE;

    /** Cost of an VoltDB node versus implementing an equivalent node in a
     * "typical" calling convention. */
    public static final double COST_MULTIPLIER = 1.0d;

    @Override public String toString() {
      return getName();
    }

    @Override
    public Class getInterface() {
      return VoltDBRel.class;
    }

    @Override
    public String getName() {
      return "VOLTDB";
    }

    @Override
    public RelTraitDef getTraitDef() {
      return ConventionTraitDef.INSTANCE;
    }

    @Override
    public boolean satisfies(RelTrait trait) {
      return this == trait;
    }

    @Override
    public void register(RelOptPlanner planner) {}

    @Override
    public boolean canConvertConvention(Convention toConvention) {
      return false;
    }

    @Override
    public boolean useAbstractConvertersForConversion(RelTraitSet fromTraits,
        RelTraitSet toTraits) {
      return false;
    }
  }