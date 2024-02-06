package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {

	Factory<ForceLaws> fl;
	Factory<Body> fb;
	PhysicsSimulator ps;

	public Controller(PhysicsSimulator pSimulator, Factory<ForceLaws> fl, Factory<Body> fb) {

		ps = pSimulator;
		this.fl = fl;
		this.fb = fb;

	}

	public void loadData(InputStream in) {

		// input del json
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));

		// cargamos el json array de groups
		JSONArray grupos = jsonInput.getJSONArray("groups");
		// a�adimos cada grupo
		for (Object g : grupos) {

			ps.addGroup(g.toString());

		}
		if (jsonInput.has("laws")) {
			// creamos un array de laws
			JSONArray laws = jsonInput.getJSONArray("laws");

			// a�adimos cada ley
			for (int i = 0; i < laws.length(); i++) {

				// nos dan el id, y la info para crear un forcelaw con el factory
				ps.setForceLaws(laws.getJSONObject(i).getString("id"),
						fl.createInstance(laws.getJSONObject(i).getJSONObject("laws")));
			}
		}

		JSONArray bodies = jsonInput.getJSONArray("bodies");
		for (int i = 0; i < bodies.length(); i++) {

			ps.addBody(fb.createInstance(bodies.getJSONObject(i)));
		}

	}

	public void run(int n, OutputStream out) {

		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("\"states\": [");
		p.println(ps.getState());
		for (int i = 0; i < n; i++) {

			ps.advance();
			p.print("," + ps.getState() + "\n");

		}

		p.println("]");
		p.println("}");

	}

	public void reset() {

		ps.reset();
	}

	public void setDeltaTime(double dt) {

		ps.setDeltaTime(dt);
	}

	public void addObserver(SimulatorObserver o) {

		ps.addObserver(o);
	}

	public void removeObserver(SimulatorObserver o) {

		ps.removeObserver(o);
	}

	public List<JSONObject> getForceLawsInfo() {

		return fl.getInfo();
	}

	public void setForcesLaws(String gId, JSONObject info) {

		ps.setForceLaws(gId, fl.createInstance(info));
	}

	public void run(int n) {

		for (int i = 0; i < n; i++) {

			ps.advance();
		}
	}


	public double getDeltaTime() {
		return ps.getDeltaTime();
	}

	public Map<String, BodiesGroup> getGroupsList() {
		return ps.getGroupsList();
	}

	public List<String> getGroupIdList() {

		return ps.getGroupIdList();
	}
}
