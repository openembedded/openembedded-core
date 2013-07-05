require bash.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=fd5d9bcabd8ed5a54a01ce8d183d592a"

PR = "r11"

SRC_URI = "${GNU_MIRROR}/bash/bash-${PV}.tar.gz;name=tarball \
           ${GNU_MIRROR}/bash/bash-3.2-patches/bash32-049;apply=yes;striplevel=0;name=patch001 \
           ${GNU_MIRROR}/bash/bash-3.2-patches/bash32-050;apply=yes;striplevel=0;name=patch002 \
           ${GNU_MIRROR}/bash/bash-3.2-patches/bash32-051;apply=yes;striplevel=0;name=patch003 \
           file://mkbuiltins_have_stringize.patch \
           file://build-tests.patch \
           file://test-output.patch \
           file://run-ptest \
          "

SRC_URI[tarball.md5sum] = "338dcf975a93640bb3eaa843ca42e3f8"
SRC_URI[tarball.sha256sum] = "128d281bd5682ba5f6953122915da71976357d7a76490d266c9173b1d0426348"
SRC_URI[patch001.md5sum] = "af571a2d164d5abdcae4499e94e8892c"
SRC_URI[patch001.sha256sum] = "b1217ed94bdb95dc878fa5cabbf8a164435eb0d9da23a392198f48566ee34a2f"
SRC_URI[patch002.md5sum] = "8443d4385d73ec835abe401d90591377"
SRC_URI[patch002.sha256sum] = "081bb03c580ecee63ba03b40beb3caf509eca29515b2e8dd3c078503609a1642"
SRC_URI[patch003.md5sum] = "15c6653042e9814aa87120098fc7a849"
SRC_URI[patch003.sha256sum] = "354886097cd95b4def77028f32ee01e2e088d58a98184fede9d3ce9320e218ef"

SRC_URI[md5sum] = "338dcf975a93640bb3eaa843ca42e3f8"
SRC_URI[sha256sum] = "128d281bd5682ba5f6953122915da71976357d7a76490d266c9173b1d0426348"
