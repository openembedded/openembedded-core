# Copyright (C) 2016 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)
#
# Functions to get metadata from the testing host used
# for analytics of test results.

from collections import OrderedDict
from collections.abc import MutableMapping
from xml.dom.minidom import parseString
from xml.etree.ElementTree import Element, tostring

from oeqa.utils.commands import runCmd, get_bb_vars

def get_os_release():
    """Get info from /etc/os-release as a dict"""
    data = OrderedDict()
    os_release_file = '/etc/os-release'
    if not os.path.exists(os_release_file):
        return None
    with open(os_release_file) as fobj:
        for line in fobj:
            key, value = line.split('=', 1)
            data[key.strip().lower()] = value.strip().strip('"')
    return data

def metadata_from_bb():
    """ Returns test's metadata as OrderedDict.

        Data will be gathered using bitbake -e thanks to get_bb_vars.
    """

    info_dict = OrderedDict()
    hostname = runCmd('hostname')
    info_dict['hostname'] = hostname.output
    data_dict = get_bb_vars()

    info_dict['machine'] = data_dict['MACHINE']

    # Distro information
    info_dict['distro'] = {'id': data_dict['DISTRO'],
                           'version_id': data_dict['DISTRO_VERSION'],
                           'pretty_name': '%s %s' % (data_dict['DISTRO'], data_dict['DISTRO_VERSION'])}

    # Host distro information
    os_release = get_os_release()
    if os_release:
        info_dict['host_distro'] = OrderedDict()
        for key in ('id', 'version_id', 'pretty_name'):
            if key in os_release:
                info_dict['host_distro'][key] = os_release[key]

    info_dict['layers'] = get_layers(data_dict['BBLAYERS'])
    return info_dict

def metadata_from_data_store(d):
    """ Returns test's metadata as OrderedDict.

        Data will be collected from the provided data store.
    """
    # TODO: Getting metadata from the data store would
    # be useful when running within bitbake.
    pass

def get_layers(layers):
    """ Returns layer name, branch, and revision as OrderedDict. """
    from git import Repo, InvalidGitRepositoryError, NoSuchPathError

    layer_dict = OrderedDict()
    for layer in layers.split():
        layer_name = os.path.basename(layer)
        layer_dict[layer_name] = OrderedDict()
        try:
            repo = Repo(layer, search_parent_directories=True)
        except (InvalidGitRepositoryError, NoSuchPathError):
            continue
        layer_dict[layer_name]['commit'] = repo.head.commit.hexsha
        try:
            layer_dict[layer_name]['branch'] = repo.active_branch.name
        except TypeError:
            layer_dict[layer_name]['branch'] = '(nobranch)'
    return layer_dict

def write_metadata_file(file_path, metadata):
    """ Writes metadata to a XML file in directory. """

    xml = dict_to_XML('metadata', metadata)
    xml_doc = parseString(tostring(xml).decode('UTF-8'))
    with open(file_path, 'w') as f:
        f.write(xml_doc.toprettyxml())

def dict_to_XML(tag, dictionary):
    """ Return XML element converting dicts recursively. """

    elem = Element(tag)
    for key, val in dictionary.items():
        if isinstance(val, MutableMapping):
            child = (dict_to_XML(key, val))
        else:
            child = Element(key)
            child.text = str(val)
        elem.append(child)
    return elem
