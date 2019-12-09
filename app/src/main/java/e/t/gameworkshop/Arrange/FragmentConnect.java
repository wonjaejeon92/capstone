package e.t.gameworkshop.Arrange;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.List;
import e.t.gameworkshop.AudioService_Click;
import e.t.gameworkshop.Popup_ConnectActivity;
import e.t.gameworkshop.Program;
import e.t.gameworkshop.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentConnect.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentConnect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentConnect extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ListView ListView_program;
    private static Adapter_ProgramConnectList Adapter_programConnectList;
    private static List<Program> List_program;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentConnect() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentConnect.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentConnect newInstance(String param1, String param2) {
        FragmentConnect fragment = new FragmentConnect();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView_program = (ListView) getView().findViewById(R.id.listConnect);
        List_program = Game_ArrangeActivity.getPrograms();
        Adapter_programConnectList = new Adapter_ProgramConnectList(getActivity(), List_program);
        ListView_program.setAdapter(Adapter_programConnectList);

        ListView_program.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), Popup_ConnectActivity.class);
                getActivity().startActivity(intent);
                Popup_ConnectActivity.setIndexProgram(i);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // 클릭 효과음
    public void ClickSound() {
        if (AudioService_Click.mute == false) {
            Intent intent_audio = new Intent(getActivity(), AudioService_Click.class);
            getActivity().startService(intent_audio);
        }
    }

    public static Adapter_ProgramConnectList getAdapter_programConnectList() {
        return Adapter_programConnectList;
    }
}
