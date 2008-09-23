require intltool_${PV}.bb

PR = "r1"

inherit native
DEPENDS = "libxml-parser-perl-native"

export PERL = "/usr/bin/env perl"
SRC_URI_append = " file://intltool-nowarn.patch;patch=1"

RRECOMMENDS = ""
