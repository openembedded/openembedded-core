require bash.inc

PR = "r1"

SRC_URI = "${GNU_MIRROR}/bash/${BPN}-${PV}.tar.gz;name=tarball \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-001;apply=yes;striplevel=0;name=patch001 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-002;apply=yes;striplevel=0;name=patch002 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-003;apply=yes;striplevel=0;name=patch003 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-004;apply=yes;striplevel=0;name=patch004 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-005;apply=yes;striplevel=0;name=patch005 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-006;apply=yes;striplevel=0;name=patch006 \
           http://ftp.gnu.org/gnu/bash/bash-4.1-patches/bash41-007;apply=yes;striplevel=0;name=patch007 \
           "

SRC_URI[tarball.md5sum] = "9800d8724815fd84994d9be65ab5e7b8"
SRC_URI[tarball.sha256sum] = "3f627124a83c6d34db503a923e20710d370573a29dd5d11d6f116d1aee7be1da"

SRC_URI[patch001.md5sum] = "582dea5671b557f783e18629c2f77b68"
SRC_URI[patch001.sha256sum] = "a6e47fa108f853d0f0999520509c11680d37c8b7823b92b96d431766dd620278"

SRC_URI[patch002.md5sum] = "118d465095d4a4706eb1d34696a2666a"
SRC_URI[patch002.sha256sum] = "322e229de186b3bd87dedabfbad8386716f103e87ff00cd1b2db844e0dff78f8"

SRC_URI[patch003.md5sum] = "120f7cf039a40d35fe375e59d6f17adc"
SRC_URI[patch003.sha256sum] = "91763dddbbb98c3ff7deb3faea3b3ad6e861e7bfd2e46c045c0d1d85d1b3256d"

SRC_URI[patch004.md5sum] = "336ee037fc2cc1e2350b05097fbdc87c"
SRC_URI[patch004.sha256sum] = "78c063ba34c1f390a5bf2e5727624ca2e253bbef49ce187cabb240eee7f4ff9e"

SRC_URI[patch005.md5sum] = "9471e666797f0b03eb2175ed752a9550"
SRC_URI[patch005.sha256sum] = "519639d8d1664be74d7ec15879d16337fe8c71af8d648b02f84ccdd4fb739c1a"

SRC_URI[patch006.md5sum] = "fb80ccd58cb1e34940f3adf4ce6e4a1e"
SRC_URI[patch006.sha256sum] = "5986abcf33c0b087bd5670f1ae6a6400a8ce6ab7e7c4de18b9826d0ee10f2c49"

SRC_URI[patch007.md5sum] = "192a8b161d419a1d0d211169f1d1046e"
SRC_URI[patch007.sha256sum] = "74012a2c28ba4fb532c3eb69155ac870aac8d53990fa4e1e52cdc173d4c205a7"

