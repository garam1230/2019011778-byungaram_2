package org.bimserver.database.actions;

/******************************************************************************
 * Copyright (C) 2009-2019  BIMserver.org
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
 * along with this program.  If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/

import org.bimserver.BimserverDatabaseException;
import org.bimserver.database.BimserverLockConflictException;
import org.bimserver.database.DatabaseSession;
import org.bimserver.database.OldQuery;
import org.bimserver.models.log.AccessMethod;
import org.bimserver.models.store.RenderEnginePluginConfiguration;
import org.bimserver.models.store.StorePackage;
import org.bimserver.shared.exceptions.UserException;

public class UpdateRenderEngineDatabaseAction extends UpdateDatabaseAction<RenderEnginePluginConfiguration> {

	private final RenderEnginePluginConfiguration renderEngine;

	public UpdateRenderEngineDatabaseAction(DatabaseSession databaseSession, AccessMethod accessMethod, RenderEnginePluginConfiguration renderEngine) {
		super(databaseSession, accessMethod, renderEngine);
		this.renderEngine = renderEngine;
	}
	
	@Override
	public Void execute() throws UserException, BimserverLockConflictException, BimserverDatabaseException {
		RenderEnginePluginConfiguration oldRenderEngine = getDatabaseSession().get(StorePackage.eINSTANCE.getRenderEnginePluginConfiguration(), renderEngine.getOid(), OldQuery.getDefault());
		if (oldRenderEngine.getEnabled() && !renderEngine.getEnabled() && !renderEngine.getSerializers().isEmpty()) {
			throw new UserException("Cannot disable render engine with serializers");
		}
		return super.execute();
	}
}