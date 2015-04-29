require git.inc

SRC_URI[tarball.md5sum] = "370cc33cc0a77a1242b95cc703228162"
SRC_URI[tarball.sha256sum] = "250ebf19c758bcf848f23f42bc632282ba8828b8965a306ad006bfb36ac1499a"
SRC_URI[manpages.md5sum] = "4c3b585c74bd937b80e04cba6db71da2"
SRC_URI[manpages.sha256sum] = "079fcd6b8dad124960578fe91964a1185c99cdf024149ad50bde141600b21628"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
