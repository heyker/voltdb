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
package org.voltdb.planner;

import java.util.ArrayList;
import java.util.List;

import org.voltdb.catalog.Table;

public class WithClause {

    public static class WithElement {
        private final Table m_schema;
        private final AbstractParsedStmt m_baseQuery;
        private final AbstractParsedStmt m_recursiveQuery;
        public WithElement(Table schema,
                           AbstractParsedStmt baseQuery,
                           AbstractParsedStmt recursiveQuery) {
            m_schema = schema;
            m_baseQuery = baseQuery;
            m_recursiveQuery = recursiveQuery;
        }
        public final Table getSchema() {
            return m_schema;
        }
        public final AbstractParsedStmt getBaseQuery() {
            return m_baseQuery;
        }
        public final AbstractParsedStmt getRecursiveQuery() {
            return m_recursiveQuery;
        }
    }

    public WithClause(boolean isRecursive) {
        m_isRecursive = isRecursive;
    }
    private final boolean m_isRecursive;
    private final List<WithElement> m_withElements = new ArrayList<>();
    public final boolean isRecursive() {
        return m_isRecursive;
    }
    public final List<WithElement> getWithElements() {
        return m_withElements;
    }
}