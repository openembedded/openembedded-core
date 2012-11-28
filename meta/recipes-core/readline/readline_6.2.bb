require readline.inc

PR = "r4"

SRC_URI += "${GNU_MIRROR}/readline/readline-6.2-patches/readline62-001;name=patch1;apply=yes;striplevel=0 \
${GNU_MIRROR}/readline/readline-6.2-patches/readline62-002;name=patch2;apply=yes;striplevel=0 \
${GNU_MIRROR}/readline/readline-6.2-patches/readline62-003;name=patch3;apply=yes;striplevel=0 \
${GNU_MIRROR}/readline/readline-6.2-patches/readline62-004;name=patch4;apply=yes;striplevel=0 \
"

SRC_URI[archive.md5sum] = "67948acb2ca081f23359d0256e9a271c"
SRC_URI[archive.sha256sum] = "79a696070a058c233c72dd6ac697021cc64abd5ed51e59db867d66d196a89381"

SRC_URI[patch1.md5sum]    = "83287d52a482f790dfb30ec0a8746669"
SRC_URI[patch1.sha256sum] = "38a86c417437692db01069c8ab40a9a8f548e67ad9af0390221b024b1c39b4e3"
SRC_URI[patch2.md5sum]    = "0665020ea118e8434bd145fb71f452cc"
SRC_URI[patch2.sha256sum] = "1e6349128cb573172063ea007c67af79256889c809973002ca66c5dfc503c7d4"
SRC_URI[patch3.md5sum]    = "c9d5d79718856e711667dede87cb7622"
SRC_URI[patch3.sha256sum] = "cb2131ff352d6e5f82edc09755191f74220b15f026bdb6c52624931c79622374"
SRC_URI[patch4.md5sum]    = "c08e787f50579ce301075c523fa660a4"
SRC_URI[patch4.sha256sum] = "09bd342479ea5bb8b6411bfdf7d302fab2e521d1d241bcb8344d3bad5d9f5476"
