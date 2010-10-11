require connman.inc
PR = "r2"

EXTRA_OECONF += "\
  ac_cv_path_WPASUPPLICANT=/usr/sbin/wpa_supplicant \
  --disable-gtk-doc \
  --enable-debug \
  --enable-threads \
  --enable-loopback \
  --enable-ethernet \
  --enable-wifi \
  --enable-bluetooth \
  --enable-dnsproxy \
  --disable-dhclient \
  --enable-test \
  --disable-udev \
  --disable-polkit \
  --enable-client \
  --prefix=/usr --sysconfdir=/etc --localstatedir=/var"

SRC_URI  = "\
  http://www.kernel.org/pub/linux/network/connman/connman-${PV}.tar.gz \
  file://fix-shutdown-ap-disconnect.patch \
  file://connman \
"
