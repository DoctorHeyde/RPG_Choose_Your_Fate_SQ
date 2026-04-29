import { useState } from "react";
import { useTypewriter } from "../../hooks/useTypewriter";
import type { Scene } from "../../types/general";
import "./ShowDialog.css";

type Props = {
  nextscene: Scene;
  changeScene: (id: number) => void;
};

export function ShowDialog({ nextscene, changeScene }: Props) {
  const [index, setIndex] = useState(0);

  const text = nextscene.dialog[index];
  const typed = useTypewriter(text);

  function nextLine() {
    if (index < nextscene.dialog.length - 1) {
      setIndex(index + 1);
    }
  }

  return (
    <div className="App" onClick={nextLine}>
      <img
        src={nextscene.img}
        alt="scene"
        style={{ width: "100vw", height: "100vh", objectFit: "contain" }}
      />

      <div className="dialogContainer">
        <div className="dialogTextContainer">
          {typed}
        </div>
      </div>

      <div className="dialogChoiceContainer">
        {nextscene.choices.map((choice: any) => (
          <div
            key={choice.id}
            className="choice"
            onClick={() => changeScene(choice.next)}
          >
            {choice.name}
          </div>
        ))}
      </div>
    </div>
  );
}