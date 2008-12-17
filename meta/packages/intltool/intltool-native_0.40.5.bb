require intltool_${PV}.bb

PR = "r2"

inherit native
DEPENDS = "libxml-parser-perl-native"

export PERL = "/usr/bin/env perl"
SRC_URI_append = " file://intltool-nowarn-0.40.0.patch;patch=1"

RRECOMMENDS = ""
