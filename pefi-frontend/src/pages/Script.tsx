import Editor, { useMonaco } from "@monaco-editor/react";
import React, { useRef } from "react";
import api, { JavaScript } from "../api/api";
import { usePefiStore } from "../store";
import { useEventObservable } from "../util/util";

const LOADING = Symbol();
export type ScriptsState = "SAVED" | "CHANGED" | "SAVING"

export default function Scripts() {
  const [serverScript, setServerScript] = React.useState<JavaScript | typeof LOADING>(LOADING);
  const [script, setScript] = React.useState<JavaScript | typeof LOADING>(LOADING);
  const scriptState = usePefiStore(state => state.scriptState);
  const setScriptState = usePefiStore(state => state.setScriptState);
  const scriptRef = useRef<typeof script>();

  scriptRef.current = script;

  React.useEffect(() => {
    api.loadScript().then(response => {
      setScript(response.script);
      setServerScript(response.script);
    });
  }, []);

  const onChange = (newVal: string) => {
    setScript(newVal);
    if (scriptState !== "SAVING") {
      if (scriptState === "SAVED" && newVal !== serverScript) {
        setScriptState("CHANGED");
      } else if (scriptState === "CHANGED" && newVal === serverScript) {
        setScriptState("SAVED");
      }
    }
  }


  if (script === LOADING) {
    return null;
  }

  return (
    <Editor
      height="100vh"
      defaultLanguage='javascript'
      theme='vs-dark'
      value={script}
      onChange={ev => ev ? onChange(ev) : undefined}
      onMount={(editor, monaco) => {
        editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyS, () => {
          const savingScript = scriptRef.current
          if (savingScript !== LOADING && savingScript) {
            setScriptState("SAVING");
            api.saveScript({ script: savingScript }).then(() => {
              setServerScript(savingScript);
              setScriptState("SAVED");
            })
          }
        })
      }}
    />
  );
}