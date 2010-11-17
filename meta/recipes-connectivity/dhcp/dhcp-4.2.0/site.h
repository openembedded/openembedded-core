/*
 * define config file location in ${S}/includes/site.h
 * still need to take care of installation path (${sysconfdir}/dhcpd.conf)
 *
 * 7/22/2010 - qhe
 */

/* Define this if you want DNS update functionality to be available. */

#define NSUPDATE

/* Define this if you aren't debugging and you want to save memory
   (potentially a _lot_ of memory) by allocating leases in chunks rather
   than one at a time. */

#define COMPACT_LEASES


/* local */
#define _PATH_DHCPD_CONF     "/etc/dhcp/dhcpd.conf"
#define _PATH_DHCLIENT_CONF  "/etc/dhcp/dhclient.conf"
