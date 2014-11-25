deltask do_fetch
deltask do_unpack
deltask do_patch

do_configure[depends] += "gcc-source:do_preconfigure"
do_populate_lic[depends] += "gcc-source:do_unpack"
