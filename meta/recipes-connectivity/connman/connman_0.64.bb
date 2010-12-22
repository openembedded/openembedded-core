require connman.inc
PR = "r0"

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

SRC_URI[md5sum] = "1328b04632ad279f991abe457b207b9d"
SRC_URI[sha256sum] = "46fd36917f53ae20cfed562406a37f9c677f65a57467cac18ce491fa089da325"
