require git.inc

SRC_URI[tarball.md5sum] = "f122606aee328de3e1019cde720cb0aa"
SRC_URI[tarball.sha256sum] = "a5d9e3e340a3e5a297092430752f61a9ae5b8b5e4ac042b0341d17189e653456"
SRC_URI[manpages.md5sum] = "285b126907d59647248577a7df01c44c"
SRC_URI[manpages.sha256sum] = "15afc42909078909ca7a48d73b7cb358b37f734b81fc3a0c6438be183f05480f"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
