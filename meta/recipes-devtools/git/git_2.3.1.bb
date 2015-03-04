require git.inc

SRC_URI[tarball.md5sum] = "a6673b9c297ff16cd573c4ff0bba34af"
SRC_URI[tarball.sha256sum] = "a447b8912ab6e9efd05bdd2e4bbb1e7f55498b0d2b92e03319c06efea7b6e46f"
SRC_URI[manpages.md5sum] = "aed07476a8323fa0be964a310f4be402"
SRC_URI[manpages.sha256sum] = "89b4c81306fe77ea745292586efba42507bacced079e3ca015e85fbe244d13b8"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
