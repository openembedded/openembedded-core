require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "af7f48ebe09eef41504241d4a845610b"
SRC_URI[tarball.sha256sum] = "c4220a6a24de853044702d9727204ef6bf254012d64e9e0f22ef46a63ec2dbe4"
SRC_URI[manpages.md5sum] = "b4bd9c649e263240035708ac28a7b680"
SRC_URI[manpages.sha256sum] = "9a84a7e7b8ea7272fac103a1c4b390872faf31a96754aa41e76ed48c437b382f"
