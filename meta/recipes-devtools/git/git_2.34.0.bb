require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.sha256sum] = "0ce6222bfd31938b29360150286b51c77c643fa97740b1d35b6d1ceef8b0ecd7"
SRC_URI[manpages.sha256sum] = "fe66a69244def488306c3e05c1362ea53d8626d2a7e57cd7311df2dab1ef8356"

