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

from oe.lsb import distro_identifier
from oeqa.utils.commands import runCmd, get_bb_var, get_bb_vars

metadata_vars = ['MACHINE', 'DISTRO', 'DISTRO_VERSION']

def metadata_from_bb():
    """ Returns test's metadata as OrderedDict.

        Data will be gathered using bitbake -e thanks to get_bb_vars.
    """

    info_dict = OrderedDict()
    hostname = runCmd('hostname')
    info_dict['hostname'] = hostname.output
    data_dict = get_bb_vars(metadata_vars)
    for var in metadata_vars:
        info_dict[var.lower()] = data_dict[var]
    host_distro= distro_identifier()
    host_distro, _, host_distro_release = host_distro.partition('-')
    info_dict['host_distro'] = host_distro
    info_dict['host_distro_release'] = host_distro_release
    info_dict['layers'] = get_layers(get_bb_var('BBLAYERS'))
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
            revision, branch = repo.head.object.name_rev.split()
            layer_dict[layer_name]['branch'] = branch
            layer_dict[layer_name]['revision'] = revision
        except (InvalidGitRepositoryError, NoSuchPathError):
            layer_dict[layer_name]['branch'] = 'unknown'
            layer_dict[layer_name]['revision'] = 'unknown'
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
