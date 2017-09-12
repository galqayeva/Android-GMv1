package next.whatsnext.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
class SectionPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments=new ArrayList<>();
    private List<String> fragmentTitle = new ArrayList<>();

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment f, String Title){
        fragments.add(f);
        fragmentTitle.add(Title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
