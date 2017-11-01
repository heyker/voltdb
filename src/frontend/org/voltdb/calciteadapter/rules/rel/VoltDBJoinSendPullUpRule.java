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

package org.voltdb.calciteadapter.rules.rel;

import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.rex.RexNode;
import org.voltdb.calciteadapter.VoltDBPartitioning;
import org.voltdb.calciteadapter.rel.VoltDBNLJoin;
import org.voltdb.calciteadapter.rel.LogicalSend;

public class VoltDBJoinSendPullUpRule extends RelOptRule {

      public static final VoltDBJoinSendPullUpRule INSTANCE = new VoltDBJoinSendPullUpRule();

      private VoltDBJoinSendPullUpRule() {
          super(operand(VoltDBNLJoin.class, operand(LogicalSend.class, any()),
                  operand(LogicalSend.class, any())));
      }

      @Override
      public boolean matches(RelOptRuleCall call) {
          VoltDBNLJoin join = call.rel(0);
          LogicalSend left = call.rel(1);
          LogicalSend right = call.rel(2);

          VoltDBPartitioning leftPartitioning = left.getPartitioning();
          VoltDBPartitioning rightPartitioning = right.getPartitioning();
          RexNode joinCondition = join.getCondition();
          boolean innerJoin = join.getJoinType() == JoinRelType.INNER;
          boolean compartiblePartitions = leftPartitioning.isCompartible(rightPartitioning, joinCondition);
          return innerJoin && compartiblePartitions;
      }

      @Override
      public void onMatch(RelOptRuleCall call) {
          VoltDBNLJoin join = call.rel(0);
          LogicalSend left = call.rel(1);
          LogicalSend right = call.rel(2);

          VoltDBPartitioning leftPartitioning = left.getPartitioning();
          VoltDBPartitioning rightPartitioning = right.getPartitioning();

          RexNode joinCondition = join.getCondition();
          RelNode newJoin = join.copy(left.getInput(), right.getInput());
          VoltDBPartitioning combinePartitioning = leftPartitioning.mergeWith(rightPartitioning, joinCondition);
          LogicalSend combinedSend = new LogicalSend(
                  join.getCluster(),
                  join.getTraitSet(),
                  newJoin,
                  combinePartitioning,
                  Math.max(left.getLevel(), right.getLevel()) + 1);

          call.transformTo(combinedSend);
      }

  }