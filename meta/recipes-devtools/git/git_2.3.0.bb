require git.inc

SRC_URI[tarball.md5sum] = "edf994cf34cd3354dadcdfa6b4292335"
SRC_URI[tarball.sha256sum] = "ba2fe814e709a5d0f034ebe82083fce7feed0899b3a8c8b3adf1c5a85d1ce9ac"
SRC_URI[manpages.md5sum] = "620797eb73b281d0706979ae8038bbd7"
SRC_URI[manpages.sha256sum] = "8aa4d1e5d7bbf5641a9de92279369d9b20cc266ba7b2888104efa40e80b53559"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
