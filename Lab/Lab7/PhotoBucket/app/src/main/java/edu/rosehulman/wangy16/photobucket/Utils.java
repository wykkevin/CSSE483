package edu.rosehulman.wangy16.photobucket;

public class Utils {
    public static String randomImageUrl() {
        String[] urls = new String[]{
                "https://pre00.deviantart.net/a183/th/pre/f/2016/214/d/7/ninetails_alola_by_theblacksavior-dacepsb.jpg",
                "https://pre00.deviantart.net/4971/th/pre/f/2016/214/f/d/vulpix_alola_by_theblacksavior-dacepq8.jpg",
                "https://pre00.deviantart.net/be30/th/pre/f/2016/207/c/6/lunala_by_theblacksavior-dabejf4.jpg",
                "https://pre00.deviantart.net/ef61/th/pre/f/2015/095/5/6/mega_diancie_by_theblacksavior-d8ojpvp.jpg",
                "https://pre00.deviantart.net/4520/th/pre/f/2014/341/f/6/infernape_by_theblacksavior-d8904qi.jpg",
                "https://pre00.deviantart.net/a7b4/th/pre/f/2014/259/7/5/mega_mawile_by_theblacksavior-d7zgl13.jpg",
                "https://pre00.deviantart.net/7404/th/pre/f/2014/258/e/a/gardevoir_by_theblacksavior-d7zbsnf.jpg",
                "https://pre00.deviantart.net/4075/th/pre/f/2014/183/c/6/mega_sableye_by_theblacksavior-d7oy1ln.jpg",
                "https://img00.deviantart.net/22a4/i/2013/117/2/0/jarachi_by_theblacksavior-d639ilh.jpg"
        };
        return urls[(int) (Math.random() * urls.length)];
    }
}





