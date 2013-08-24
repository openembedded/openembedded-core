#!/usr/bin/python -tt
#
# Copyright (c) 2010, 2011 Intel, Inc.
#
# This program is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the Free
# Software Foundation; version 2 of the License
#
# This program is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
# or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc., 59
# Temple Place - Suite 330, Boston, MA 02111-1307, USA.

import os
import urlparse

_my_proxies = {}
_my_noproxy = None
_my_noproxy_list = []

def set_proxy_environ():
    global _my_noproxy, _my_proxies
    if not _my_proxies:
        return
    for key in _my_proxies.keys():
        os.environ[key + "_proxy"] = _my_proxies[key]
    if not _my_noproxy:
        return
    os.environ["no_proxy"] = _my_noproxy

def unset_proxy_environ():
    for env in ('http_proxy',
                'https_proxy',
                'ftp_proxy',
                'all_proxy'):
        if env in os.environ:
            del os.environ[env]

        ENV=env.upper()
        if ENV in os.environ:
            del os.environ[ENV]

def _set_proxies(proxy = None, no_proxy = None):
    """Return a dictionary of scheme -> proxy server URL mappings.
    """

    global _my_noproxy, _my_proxies
    _my_proxies = {}
    _my_noproxy = None
    proxies = []
    if proxy:
        proxies.append(("http_proxy", proxy))
    if no_proxy:
        proxies.append(("no_proxy", no_proxy))

    # Get proxy settings from environment if not provided
    if not proxy and not no_proxy:
        proxies = os.environ.items()

        # Remove proxy env variables, urllib2 can't handle them correctly
        unset_proxy_environ()

    for name, value in proxies:
        name = name.lower()
        if value and name[-6:] == '_proxy':
            if name[0:2] != "no":
                _my_proxies[name[:-6]] = value
            else:
                _my_noproxy = value

def _ip_to_int(ip):
    ipint=0
    shift=24
    for dec in ip.split("."):
        ipint |= int(dec) << shift
        shift -= 8
    return ipint

def _int_to_ip(val):
    ipaddr=""
    shift=0
    for i in range(4):
        dec = val >> shift
        dec &= 0xff
        ipaddr = ".%d%s" % (dec, ipaddr)
        shift += 8
    return ipaddr[1:]

def _isip(host):
    if host.replace(".", "").isdigit():
        return True
    return False

def _set_noproxy_list():
    global _my_noproxy, _my_noproxy_list
    _my_noproxy_list = []
    if not _my_noproxy:
        return
    for item in _my_noproxy.split(","):
        item = item.strip()
        if not item:
            continue

        if item[0] != '.' and item.find("/") == -1:
            # Need to match it
            _my_noproxy_list.append({"match":0,"needle":item})

        elif item[0] == '.':
            # Need to match at tail
            _my_noproxy_list.append({"match":1,"needle":item})

        elif item.find("/") > 3:
            # IP/MASK, need to match at head
            needle = item[0:item.find("/")].strip()
            ip = _ip_to_int(needle)
            netmask = 0
            mask = item[item.find("/")+1:].strip()

            if mask.isdigit():
                netmask = int(mask)
                netmask = ~((1<<(32-netmask)) - 1)
                ip &= netmask
            else:
                shift=24
                netmask=0
                for dec in mask.split("."):
                    netmask |= int(dec) << shift
                    shift -= 8
                ip &= netmask

            _my_noproxy_list.append({"match":2,"needle":ip,"netmask":netmask})

def _isnoproxy(url):
    (scheme, host, path, parm, query, frag) = urlparse.urlparse(url)

    if '@' in host:
        user_pass, host = host.split('@', 1)

    if ':' in host:
        host, port = host.split(':', 1)

    hostisip = _isip(host)
    for item in _my_noproxy_list:
        if hostisip and item["match"] <= 1:
            continue

        if item["match"] == 2 and hostisip:
            if (_ip_to_int(host) & item["netmask"]) == item["needle"]:
                return True

        if item["match"] == 0:
            if host == item["needle"]:
                return True

        if item["match"] == 1:
            if host.rfind(item["needle"]) > 0:
                return True

    return False

def set_proxies(proxy = None, no_proxy = None):
    _set_proxies(proxy, no_proxy)
    _set_noproxy_list()
    set_proxy_environ()

def get_proxy_for(url):
    if url.startswith('file:') or _isnoproxy(url):
        return None

    type = url[0:url.index(":")]
    proxy = None
    if _my_proxies.has_key(type):
        proxy = _my_proxies[type]
    elif _my_proxies.has_key("http"):
        proxy = _my_proxies["http"]
    else:
        proxy = None

    return proxy
