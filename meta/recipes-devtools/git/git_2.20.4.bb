require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "6f524e37186a79848a716e2a91330868"
SRC_URI[tarball.sha256sum] = "92719084d7648b69038ea617a3bc45ec74f60ed7eef753ae2ad84b6f0b268e9a"
SRC_URI[manpages.md5sum] = "dceabcda244042a06ed4cabd754627a5"
SRC_URI[manpages.sha256sum] = "72fdd1799756b1240921d10eb5c67de9a651b44d429ba7293929c9d5344ad3e0"
