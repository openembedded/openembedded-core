#
# Known good source dates/revisions/tags to use, *only* for where Moblin deviates
# from Poky (or to lock down SCM based packages):
#
PREFERRED_VERSION_eds-dbus ?= "2.20.0"
PREFERRED_VERSION_dbus ?= "1.2.14"
PREFERRED_VERSION_dbus-native ?= "1.2.14"
PREFERRED_VERSION_gtk+ ?= "2.16.4"
PREFERRED_VERSION_udev ?= "145"

PREFERRED_VERSION_mesa-dri ?= "7.5"
PREFERRED_VERSION_xf86-video-intel ?= "2.12.0"
PREFERRED_VERSION_xserver-xf86-dri-lite ?= "1.6.0"
PREFERRED_VERSION_libxcb ?= "1.3"
PREFERRED_VERSION_xcb-proto ?= "1.5"
PREFERRED_VERSION_libx11-trim ?= "1.2.1"

PREFERRED_VERSION_libdrm ?= ""
PREFERRED_VERSION_xf86-input-keyboard ?= ""
PREFERRED_VERSION_dri2proto ?= ""
SRCREV_pn-libdrm ?= "3f3c5be6f908272199ccf53f108b1124bfe0a00e"
SRCREV_pn-xf86-input-keyboard ?= "3e28d68b50d291938734e9684b8296ca864f3892"
SRCREV_pn-dri2proto ?= "66c56ab10d917e3f47f93178d7eac6430970d3c4"

SRCREV_pn-anerley ?= "0cb9ac6b82ff6658ae10d603bdfba04ef3df979e"
SRCREV_pn-bickley ?= "761816f24e2aeb329d4f7270a2af324f85b7be8f"
SRCREV_pn-bisho ?= "53cc5951be36cee818bef3beb5770f10aabdc7f1"
SRCREV_pn-bognor-regis ?= "6b6cce0cb90ea47a3cc4ac2804c8b5664530ca80"
SRCREV_pn-carrick ?= "0451a081309f11dd5e9b6e41778ab878d862cbd0"


SRCREV_pn-connman ?= "6af0b24817b8712c755ef24ed0ee4c5e84550657"
SRCREV_pn-connman-gnome ?= "857b149c7febe9b0ffd9790050a288f018644eda"
SRCREV_pn-contacts ?= "c8e6b16222d5bc9d8f60ae6594af4933613a4e25"


# Still tosort:

SRCREV_pn-clutter ?= "e860d939f9d9611dcf5b8c6e70272b6318a4ba94"
1.0.0
SRCREV_pn-clutter-1.0 ?= "420ed353b53bd624331bd249d7df98d14714b504"
1.0.0
SRCREV_pn-clutter-box2d ?= "2bcf5b836548dc4909e13b8ae7f60dc64c93d559"
clutter-box2d-0.9~20090609
SRCREV_pn-clutter-gst ?= "db9bcf9d6f3a7d0f34747bed0f3ab28d7eb8f6ab"
gst-0.10~20090729
SRCREV_pn-clutter-gtk ?= "5dfc07761adf3ab3d1c9ee97b92bc4a65473db36"
0.10.2
SRCREV_pn-clutter-imcontext ?= "2cb6f0ff153db639a489e58e36802e0e928b1968"
0.1
SRCREV_pn-clutter-mozembed ?= "5a5329425aec7fe7749e97fd8c430457259f6165"
0.0~20090731


SRCREV_pn-dalston ?= "b4d03283cb8aa2b76014eeb20e78cd13c14012ae"
SRCREV_pn-dates ?= "966969063535da0786b7483c1bcd427df8a0d87d"
SRCREV_pn-gconnman ?= "3d951640146d08249e0219cbae6a4c2da787a878"
SRCREV_pn-hornsey ?= "39cb0cd0ee3526cba27aeebcc3e28dee6add136a"
SRCREV_pn-json-glib ?= "7411cadc0fdd9ffc2bd7004c9980913ac857a495"
SRCREV_pn-libccss ?= "62697eb84fe190f9b8c65fcbaae10ec3d9edfffd"
SRCREV_pn-librest ?= "ff4561e2a8c38f49127f6e3b2ce7c238a29e1571"
SRCREV_pn-libsynthesis ?= "70f0065aa3b085bdf059830f95e5b5766eecb0bb"
SRCREV_pn-moblin-gtk-engine ?= "dff58e5f1b6c111a6c06e2385624e40d687ebf4f"
SRCREV_pn-moblin-icon-theme ?= "36b7ac1e57ea6d95e6432779c4769c14fce24899"
SRCREV_pn-moblin-menus ?= "1edfdab8234ec0b4bbc83fb706a15e08aa8a957a"
SRCREV_pn-moblin-proto ?= "8f2cb524fe06555182c25b4ba3202d7b368ac0ce"
SRCREV_pn-moblin-user-skel ?= "356851e174085b874adb909e664aca321a047bcb"
SRCREV_pn-moblin-panel-applications ?= "ba6b5f353203bb02728addb08da3d249f26eedbe"
SRCREV_pn-moblin-panel-media ?= "4e93c6da0bbae8d1d0a5d3b1410761679aa7b83b"
SRCREV_pn-moblin-panel-myzone ?= "54d9bee3a40a7700a69582a76aba89e213410046"
SRCREV_pn-moblin-panel-pasteboard ?= "44411a4516e6ae22767662430fa37ed6a7d27a15"
SRCREV_pn-moblin-panel-people ?= "0766282d120587919718b3bcb91553af1a1a00da"
SRCREV_pn-moblin-panel-status ?= "fb10eaf16bbd064c12587ec8c734abe72a9841d3"
SRCREV_pn-moblin-web-browser ?= "a3ea71159bef93dc03762195bd5404a51db5e4a6"
SRCREV_pn-mojito ?= "cfad50252a4ad60f28b12d21e2fdb35db56984f5"
SRCREV_pn-mozilla-headless-services ?= "9091da493c679c546ec7312b653e63f9701d5c8f"
SRCREV_pn-mutter ?= "c96345dd81176f171c2d15dd9d5c5b20f334f842"
SRCREV_pn-mutter-moblin ?= "7a23e0d63cc31c12dab9de517de04b0ecf41b28a"
SRCREV_pn-mux ?= "72460e890dbb15edbf7dc193116be0dcf9794a8b"
SRCREV_pn-nbtk ?= "635f9b4f1a2897aaa85ec1285e57dc5478517c2f"
SRCREV_pn-mtpfs = "17"
SRCREV_pn-qemugl ?= "d888bbc723c00d197d34a39b5b7448660ec1b1c0"
SRCREV_pn-syncevolution ?= "29972357bf6582c318258b7bdf1feb13c0fef824"
SRCREV_pn-tasks ?= "a3c6109973c2f39190f554b49dacda7ec5231ef4"
SRCREV_pn-twitter-glib ?= "9dfca7cf38e3dddbda6dc82d0164f65814b8783e"

SRCREVMOZILLAHEADLESS ?= "1a622cb7c384"
