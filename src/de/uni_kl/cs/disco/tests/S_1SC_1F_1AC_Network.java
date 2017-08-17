/*
 * This file is part of the Disco Deterministic Network Calculator v2.4.0beta2 "Chimera".
 *
 * Copyright (C) 2013 - 2017 Steffen Bondorf
 * Copyright (C) 2017 The DiscoDNC contributors
 *
 * Distributed Computer Systems (DISCO) Lab
 * University of Kaiserslautern, Germany
 *
 * http://disco.cs.uni-kl.de/index.php/projects/disco-dnc
 *
 *
 * The Disco Deterministic Network Calculator (DiscoDNC) is free software;
 * you can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package de.uni_kl.cs.disco.tests;

import de.uni_kl.cs.disco.curves.ArrivalCurve;
import de.uni_kl.cs.disco.curves.CurvePwAffineFactory;
import de.uni_kl.cs.disco.curves.ServiceCurve;
import de.uni_kl.cs.disco.network.Flow;
import de.uni_kl.cs.disco.network.Network;
import de.uni_kl.cs.disco.network.NetworkFactory;
import de.uni_kl.cs.disco.network.Server;

public class S_1SC_1F_1AC_Network implements NetworkFactory {
    private static final int sc_R = 10;
    private static final int sc_T = 10;
    private static final int ac_r = 5;
    private static final int ac_b = 25;
    protected Server s0;
    protected Flow f0;
    private ServiceCurve service_curve = CurvePwAffineFactory.createRateLatency(sc_R, sc_T);
    private ArrivalCurve arrival_curve = CurvePwAffineFactory.createTokenBucket(ac_r, ac_b);
    private Network network;

    public S_1SC_1F_1AC_Network() {
        network = createNetwork();
    }

    public Network getNetwork() {
        return network;
    }

    public Network createNetwork() {
        network = new Network();

        s0 = network.addServer(service_curve);
        s0.setUseGamma(false);
        s0.setUseExtraGamma(false);

        try {
            f0 = network.addFlow(arrival_curve, s0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return network;
    }

    public void reinitializeCurves() {
        service_curve = CurvePwAffineFactory.createRateLatency(sc_R, sc_T);
        for (Server server : network.getServers()) {
            server.setServiceCurve(service_curve);
        }

        arrival_curve = CurvePwAffineFactory.createTokenBucket(ac_r, ac_b);
        f0.setArrivalCurve(arrival_curve);
    }
}